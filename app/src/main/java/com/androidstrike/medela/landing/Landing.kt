package com.androidstrike.medela.landing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.androidstrike.medela.R
import com.androidstrike.medela.landing.fragments.History
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.nav_header_landing.*

class Landing : AppCompatActivity() {

    private var backPressedTime: Long = 0

    override fun onBackPressed() {
        val fragment = History()
        supportFragmentManager.beginTransaction()
            .replace(R.id.auth_frame, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        setSupportActionBar(toolbar)

//        txtFullName.text = Common.user_name.toString()
//        txtEmail.text = Common.email.toString()

        val navController = Navigation.findNavController(this, R.id.fragment_container)

        val appBarConfiguration = AppBarConfiguration(navController.graph, drawer_layout)
        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration)
        NavigationUI.setupActionBarWithNavController(this,navController,drawer_layout)
        nav_view.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.fragment_container)
            .navigateUp() || super.onSupportNavigateUp()
    }
}