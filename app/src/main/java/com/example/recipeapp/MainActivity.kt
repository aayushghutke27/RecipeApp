package com.example.recipeapp

import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.recipeapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    var homeFragment = HomeFragment()
    var searchFragment = SearchFragment()
    var savedFragment = SavedFragment()
    var profileFragment = ProfileFragment()

    private lateinit var activeFragment: Fragment
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(3000)
        installSplashScreen()

//      WindowCompat.setDecorFitsSystemWindows(window, false)


        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

        if (currentUser == null) {
            navigateToAuth()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
            )
        )

        setContentView(binding.root)

//        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
//            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
//            view.setPadding(0, statusBar.top, 0, 0)
//            insets
//
//        }

//        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationBar) { view, insets ->
//            val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
//            view.setPadding(0, 0, 0, navBar.bottom)
//            insets
//
//        }


//        if(savedInstanceState == null){
//            loadFragment(HomeFragment())
//        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, profileFragment, "Profile").hide(profileFragment)
                .add(R.id.fragment_container, savedFragment, "Saved").hide(savedFragment)
                .add(R.id.fragment_container, searchFragment, "Search").hide(searchFragment)
                .add(R.id.fragment_container, homeFragment, "Home")
                .commit()

            activeFragment = homeFragment

        }


        binding.bottomNavigationBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> switchFragment(homeFragment)
                R.id.nav_search -> switchFragment(searchFragment)
                R.id.nav_saved -> switchFragment(savedFragment)
                R.id.nav_profile -> switchFragment(profileFragment)
            }
            true
        }

    }

    private fun navigateToAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun switchFragment(target: Fragment) {
        if (target == activeFragment) return
        supportFragmentManager.beginTransaction().hide(activeFragment).show(target).commit()
        activeFragment = target
    }

    fun showBottomFragment() {
        binding.bottomNavigationBar.visibility = View.VISIBLE
    }

    fun hideBottomFragment() {
        binding.bottomNavigationBar.visibility = View.GONE
    }


}


