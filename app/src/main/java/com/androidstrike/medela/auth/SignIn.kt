package com.androidstrike.medela.auth

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidstrike.medela.R
import com.androidstrike.medela.models.User
import com.androidstrike.medela.utils.Common

//import com.androidstrike.medela.osefi.models.User
//import com.androidstrike.medela.utils.Common
import com.androidstrike.medela.utils.login
import com.androidstrike.medela.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignIn : Fragment() {


    //    a firebase auth object is created to enable us create a user in the firebase console
//    we have the lateinit var called mAuth where we store the instance of the FirebaseAuth
    private var mAuth: FirebaseAuth? = null
    private var firebaseUser: FirebaseUser? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.d("EQUA", "onActivityCreated: ")

//        here we initialize the instance of the Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        firebaseUser = mAuth!!.currentUser

        button_sign_in.setOnClickListener {
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()


//            if the email and password fields are empty we display error messages
            if (email.isEmpty()) {
                et_email.error = "Email Required"
                et_email.requestFocus()
                return@setOnClickListener
            }

//            if the email pattern/format does not does match that as defined, we display error messages
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                et_email.error = "Valid Email Required"
                et_email.requestFocus()
                return@setOnClickListener
            }

//            if the password contains less than 6 characters we display error message
            if (password.isEmpty() || password.length < 6) {
                et_password.error = "6 char password required"
                et_password.requestFocus()
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        tv_new_account.setOnClickListener {
            val fragment = SignUp()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.auth_frame, fragment, fragment.javaClass.simpleName)
                ?.commit()
        }

        tv_forgot_password.setOnClickListener {
            val frag = PasswordReset()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.auth_frame, frag, frag.javaClass.simpleName)
                ?.commit()
        }

    }

    private fun loginUser(email: String, password: String) {
        progress_sign_in.visibility = View.VISIBLE

        //todo check if the username exists in the realtime database then perform login
        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener {

                if (it.isSuccessful) {
                    //login success
                    Common.currentUser = User(firebaseUser?.uid!!, firebaseUser!!.email)
                    activity?.login()

                    progress_sign_in.visibility = View.GONE


                } else {
                    it.exception?.message?.let {
                        activity?.toast(it)
                    }
                }
            }

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val usr_info = database.getReference("Users/${firebaseUser?.uid.toString()}")
        var usrModel: User?
        val usrInfoListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usrModel = snapshot.getValue(User::class.java)

                Common.user_name = usrModel!!.name
                Common.email = usrModel!!.email.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                activity?.toast("EWOOO!!!")
            }

        }
        usr_info.addListenerForSingleValueEvent(usrInfoListener)

    }
}
