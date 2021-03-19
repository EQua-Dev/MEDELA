package com.androidstrike.medela.utils

import com.androidstrike.medela.models.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object Common {
    lateinit var currentUser: User
    lateinit var email: String
    lateinit var user_name: String
    var student_level: String? = null
    var feeToPayHash: HashMap<String, Int>? = null

    var database : FirebaseDatabase = FirebaseDatabase.getInstance() // todo when you have time, change all the private database references to use this one

}