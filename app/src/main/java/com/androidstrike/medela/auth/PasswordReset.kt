package com.androidstrike.medela.auth

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidstrike.medela.R
import com.androidstrike.medela.utils.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_password_reset.*

class PasswordReset : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_reset, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_reset_password.setOnClickListener {

//           we run validation on the email
            val email = text_recover_email.text.toString().trim()

//            if the email and password fields are empty we display error messages
            if (email.isEmpty()) {
                text_recover_email.error = "Email Required"
                text_recover_email.requestFocus()
                return@setOnClickListener
            }

//            if the email pattern/format does not does match that as defined, we display error messages
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                text_recover_email.error = "Valid Email Required"
                text_recover_email.requestFocus()
                return@setOnClickListener
            }

            progressbar.visibility = View.VISIBLE

//            we then send the PasswordResetEmail to the entered email from fireBase
            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .addOnCompleteListener { task->
                    progressbar.visibility = View.GONE

                    if (task.isSuccessful){
                        activity?.toast("Check your email")
                    }else{
                        activity?.toast(task.exception?.message!!)
                    }
                }

        }

    }
}