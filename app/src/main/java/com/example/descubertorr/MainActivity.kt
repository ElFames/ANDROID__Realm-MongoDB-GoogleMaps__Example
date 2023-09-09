package com.example.descubertorr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.descubertorr.data.ServiceLocator
import com.example.descubertorr.databinding.ActivityMainBinding
import com.example.descubertorr.ui.views.LoginFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkIsLogged()
    }

    private fun checkIsLogged() {
        if(ServiceLocator.realmManager.loggedIn()) {
            CoroutineScope(Dispatchers.IO).launch {
                ServiceLocator.realmManager.configureRealm()
            }
            createBottomToolbar()
        }
        else startLoginFragment()
    }

    private fun createBottomToolbar() {
        val bottomNavigationView = binding.bottomNavigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.dashboardFragment || nd.id == R.id.mapFragment || nd.id == R.id.sitesRecyclerFragment)
                bottomNavigationView.visibility = View.VISIBLE
            else bottomNavigationView.visibility = View.GONE

        }
        bottomNavigationView.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.dashboardFragment, R.id.mapFragment, R.id.sitesRecyclerFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun startLoginFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, LoginFragment())
            setReorderingAllowed(true)
            addToBackStack("todo")
            commit()
        }
    }
}