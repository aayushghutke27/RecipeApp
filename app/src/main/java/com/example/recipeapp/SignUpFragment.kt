package com.example.recipeapp

import android.R
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.example.recipeapp.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        setUpClickListener()
        setUpTextWatcher()
    }

    private fun setUpClickListener() {

        binding.linkLogin.setOnClickListener {
            (activity as AuthActivity).loadFragment(LoginFragment())
        }

        binding.btnSignUp.setOnClickListener {
            signUpUser()
        }

    }

    private fun setUpTextWatcher() {
        binding.tfEmail.editText?.doAfterTextChanged { updateButtonChange() }
        binding.tfPassword.editText?.doAfterTextChanged { updateButtonChange() }
        binding.tfConfirmPassword.editText?.doAfterTextChanged { updateButtonChange() }
    }

    private fun updateButtonChange() {
        val email = binding.tfEmail.editText?.text.toString().trim()
        val password = binding.tfPassword.editText?.text.toString().trim()
        val confirmPassword = binding.tfConfirmPassword.editText?.text.toString().trim()

        val emailValid = isValidEmail(email)
        val passwordValid = isValidPassword(password)
        val samePassword = password == confirmPassword

        binding.btnSignUp.isEnabled = emailValid && passwordValid && samePassword

        binding.tfEmail.error =
            if (!isValidEmail(email) && email.isNotEmpty()) "Invalid Email" else null


        binding.tfPassword.error =
            if (!isValidPassword(password) && password.isNotEmpty()) "Password must be at least 6 chars, include 1 uppercase & 1 number" else null

        binding.tfConfirmPassword.error =
            if (!samePassword && confirmPassword.isNotEmpty()) "Password is not match" else null


    }

    private fun signUpUser() {

        val email = binding.tfEmail.editText?.text.toString().trim()
        val password = binding.tfPassword.editText?.text.toString().trim()
        val confirmPassword = binding.tfPassword.editText?.text.toString().trim()

//        if (!isValidEmail(email)) {
//            binding.tfEmail.error = "Enter Valid Email"
//            return
//        } else {
//            binding.tfEmail.error = null
//        }
//
//        if (!isValidPassword(password)) {
//            binding.tfPassword.error =
//                "Password must be at least 6 chars, include 1 uppercase & 1 number"
//            return
//        } else {
//            binding.tfPassword.error = null
//        }
//
//        if (confirmPassword != password) {
//            binding.tfConfirmPassword.error = "Password does not match"
//            return
//        } else {
//            binding.tfConfirmPassword.error = null
//        }

        showLoading(true)

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            showLoading(false)
            if (task.isSuccessful) {
                Snackbar.make(binding.root, "User Account Created", Snackbar.LENGTH_SHORT).show()
                (activity as AuthActivity).loadFragment(LoginFragment())
            } else {
                Snackbar.make(
                    binding.root,
                    task.exception?.message ?: "User Account not created",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }


    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {

        val regex = Regex("^(?=.*[A-Z])(?=.*[0-9]).{6,}$")
        return regex.matches(password)

    }

    private fun showLoading(show: Boolean) {
        binding.loading.visibility = if (show) View.VISIBLE else View.GONE
        binding.loading.isEnabled = !show
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}