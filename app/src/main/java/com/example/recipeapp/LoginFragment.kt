package com.example.recipeapp

import android.app.Activity
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import com.example.recipeapp.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import android.util.Log

class LoginFragment : Fragment() {

    companion object {
        private const val TAG = "LoginFragment"
    }
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient


    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result -> handleGoogleSignInResult(result)
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpClickListener()
        setUpTextWatcher()
        auth = FirebaseAuth.getInstance()
        setUpGoogleLogin()
    }

    private fun setUpGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun setUpClickListener() {

        binding.linkSignUp.setOnClickListener {
            (activity as AuthActivity).loadFragment(SignUpFragment())
        }

        binding.btnLogin.setOnClickListener {
            performUserLogin()
        }

        binding.btnGoogleSignIn.setOnClickListener {
            performGoogleLogin()
        }
    }

    private fun setUpTextWatcher() {
        binding.tfEmail.editText?.doAfterTextChanged { updateButtonChange() }
        binding.tfPassword.editText?.doAfterTextChanged { updateButtonChange() }


    }

    private fun updateButtonChange() {
        val email = binding.tfEmail.editText?.text.toString().trim()
        val password = binding.tfPassword.editText?.text.toString().trim()

        var isValid = true
        if (email.isEmpty()) {
            binding.tfEmail.error = "Enter Email Id"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tfEmail.error = "Enter valid email id"
            isValid = false
        } else {
            binding.tfEmail.error = null
        }


        if (password.isEmpty()) {
//            binding.tfPassword.error = "Enter Password"
            isValid = false
        } else if (!Regex("^(?=.*[A-Z])(?=.*[0-9]).{6,}$").matches(password)) {
//            binding.tfPassword.error =
//                "Your Password is not valid or please check your password"
            isValid = false
        } else {
            binding.tfPassword.error = null
        }

        binding.btnLogin.isEnabled = isValid
    }

    private fun performUserLogin() {

        val email = binding.tfEmail.editText?.text.toString().trim()
        val password = binding.tfPassword.editText?.text.toString().trim()

        showLoading(true)

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            showLoading(false)
            if (task.isSuccessful) {
                Snackbar.make(binding.root, "Login Successfully", Snackbar.LENGTH_SHORT).show()
                (activity as AuthActivity).navigateUpToMainActivity()
            }else{
                Snackbar.make(binding.root, "Login UnSuccessfully", Snackbar.LENGTH_SHORT).show()

            }
        }


    }

    private fun showLoading(show: Boolean) {
        binding.loginProgressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !show
    }

    private fun LoginFragment.performGoogleLogin() {
        val signInClient = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInClient)
    }
//
//    private fun handleGoogleSignInResult(result: ActivityResult) {
//
//
//        if (result.resultCode == Activity.RESULT_OK){
//
//            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).getResult(com.google.android.gms.common.api.ApiException::class.java)
//
//            val idToken = account?.idToken
//
//            if (idToken != null){
//                authenticateWithFirebase(idToken)
//            }else{
//                showError("Failed to get authentication token")
//
//            }
//        }else{
//            showError("There is no result code")
//        }
//    }

    private fun handleGoogleSignInResult(result: androidx.activity.result.ActivityResult) {
        Log.d(TAG, "Result code: ${result.resultCode}")

        when (result.resultCode) {
            Activity.RESULT_OK -> {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    val account = task.getResult(ApiException::class.java)

                    Log.d(TAG, "Google sign in succeeded for: ${account.email}")

                    val idToken = account.idToken
                    if (idToken != null) {
                        Log.d(TAG, "ID Token received successfully")
                        authenticateWithFirebase(idToken)
                    } else {
                        Log.e(TAG, "ID Token is null!")
                        showError("Failed to get authentication token")
                    }

                } catch (e: ApiException) {
                    Log.e(TAG, "ApiException status code: ${e.statusCode}", e)

                    val errorMessage = when (e.statusCode) {
                        10 -> "Configuration error. Please contact support."
                        12501 -> "Sign in was cancelled"
                        12500 -> "Sign in failed. Please try again."
                        7 -> "Network error. Check your internet connection"
                        else -> "Sign in failed (Code: ${e.statusCode})"
                    }

                    showError(errorMessage)
                }
            }

            Activity.RESULT_CANCELED -> {
                Log.d(TAG, "User cancelled sign-in")
                showMessage("Sign-in cancelled")
            }

            else -> {
                Log.e(TAG, "Unexpected result code: ${result.resultCode}")
                showError("Sign-in failed unexpectedly")
            }
        }
    }

    private fun authenticateWithFirebase(idToken: String) {

        showLoading(true)

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener {
            task ->
            showLoading(false)

            if (task.isSuccessful){
                showMessage("Successfully login with google")
                (activity as AuthActivity).navigateUpToMainActivity()

            }else{
                val error = task.exception?.message ?: "Login with google failed"
                showError(error)
            }
        }
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showMessage(message: String){
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT ).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





