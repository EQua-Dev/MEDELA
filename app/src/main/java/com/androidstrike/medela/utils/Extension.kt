package com.androidstrike.medela.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.androidstrike.medela.auth.AuthActivity
import com.androidstrike.medela.landing.Landing

fun Context.toast(message:String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.login(){
    val intent = Intent(this, Landing::class.java).apply {
        //            we add these flags to ensure that we destroy all other activities when the HomeActivity is launched
//                    this prevents the user from being taken back to the login screen onBackPressed
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}

fun Context.logout(){
    val intent = Intent(this, AuthActivity::class.java).apply {
        //            we add these flags to ensure that we destroy all other activities when the HomeActivity is launched
//                    this prevents the user from being taken back to the login screen onBackPressed
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}
