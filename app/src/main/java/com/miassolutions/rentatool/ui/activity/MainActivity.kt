package com.miassolutions.rentatool.ui.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.AppDatabase
import com.miassolutions.rentatool.core.utils.extenstions.showToast
import com.miassolutions.rentatool.core.utils.helper.LanguageHelper
import com.miassolutions.rentatool.core.utils.helper.PermissionUtils
import com.miassolutions.rentatool.core.utils.helper.showToast
import com.miassolutions.rentatool.core.utils.mockdb.MockDB
import com.miassolutions.rentatool.core.utils.mockdb.MockDB.customers
import com.miassolutions.rentatool.core.utils.mockdb.MockDB.tools
import com.miassolutions.rentatool.databinding.ActivityMainBinding
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val requiredPermissions = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_MEDIA_IMAGES
    )

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        val savedLanguage = LanguageHelper.getSavedLanguage(this)
        if (savedLanguage.isNotEmpty()){
            LanguageHelper.setLocale(this, savedLanguage)
        }


        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        if (!PermissionUtils.hasPermissions(this, requiredPermissions)) {
            PermissionUtils.requestPermissions(this, requiredPermissions)
        }



        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.stockFragment,
                R.id.customersListFragment,
                R.id.addToolFragment
            ), binding.drawerLayout
        )




        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navigationView.setupWithNavController(navController)


        binding.navigationView.menu.apply {
            findItem(R.id.aboutApp).setOnMenuItemClickListener {
                showToast(this@MainActivity, "MIAS Solutions")
                binding.drawerLayout.closeDrawers()
                true
            }
            findItem(R.id.urdu_menu).setOnMenuItemClickListener {
                switchLanguage("ur")
                binding.drawerLayout.closeDrawers()
                true
            }
            findItem(R.id.english_menu).setOnMenuItemClickListener {
                switchLanguage("en")
                binding.drawerLayout.closeDrawers()
                true
            }
        }



    }


    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun switchLanguage(language: String) {
        LanguageHelper.setLocale(this, language)
        LanguageHelper.saveLanguagePreference(this, language)
        this.recreate() // Recreate the activity to apply the new language
        showToast(this,"Switched to ${if (language == "en") "English" else "Urdu"}")
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == PermissionUtils.REQUEST_CODE) {
            val deniedPermissions = permissions.zip(grantResults.toList())
                .filter { it.second != PackageManager.PERMISSION_GRANTED }
                .map { it.first }

            if (deniedPermissions.isEmpty()) {
                showToast(this, "All permissions granted")
            } else showToast(this, "Permission denied : ${deniedPermissions.joinToString()}")
        }
    }
}