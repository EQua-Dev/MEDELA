package com.androidstrike.medela.auth

import android.os.Build
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import com.androidstrike.medela.R
import com.androidstrike.medela.models.User
import com.androidstrike.medela.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [SignUp.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUp : Fragment() {



    //    a firebase auth object is created to enable us create a user in the firebase console
//    we have the lateinit var called mAuth where we store the instance of the FirebaseAuth
    private lateinit var mAuth : FirebaseAuth
    private lateinit var database :  FirebaseDatabase
    private lateinit var table_user: DatabaseReference

    private var userId:String?=null
    private var emailAddress:String?=null
    lateinit var date_joined: String
//    lateinit var radioGroup: RadioGroup
    lateinit var selectedRadioButton: RadioButton
    private var gender: String? = null

    private var date_of_birth: String? = null
    private var blood_group: String? = null
    private var genotype: String? = null
    private var allergies: String? = null
    private var address: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_sign_up, container, false)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


//        here we initialize the instance of the Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        table_user = database.getReference().child("Users")

        button_sign_up.setOnClickListener {
           val email = et_new_email.text.toString().trim()
            val password = et_new_password.text.toString().trim()
            val confirm_password = et_new_confirm_password.text.toString().trim()
            val user_name = et_new_user_name.text.toString().trim()
            val phone = et_new_phone_number.text.toString().trim()


            val current = LocalDate.now()
            val monthString = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
            date_joined = current.format(monthString).toString()


            val selectedRadioButtonId: Int = radio_group.checkedRadioButtonId
            if (selectedRadioButtonId == -1){
                rb_male.error = "Please select Gender"
                radio_group.requestFocus()
            }
            if (selectedRadioButtonId != -1){
                selectedRadioButton = requireView().findViewById(selectedRadioButtonId)
                gender= selectedRadioButton.text.toString()
            }
            if (user_name.isEmpty()){
                et_new_user_name.error = "Name Required"
                et_new_user_name.requestFocus()
                return@setOnClickListener
            }
//            if the email and password fields are empty we display error messages
            if (email.isEmpty()){
                et_new_email.error = "Email Required"
                et_new_email.requestFocus()
                return@setOnClickListener
            }

//            if the email pattern/format does not does match that as defined, we display error messages
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                et_new_email.error = "Valid Email Required"
                et_new_email.requestFocus()
                return@setOnClickListener
            }

            if (phone.isEmpty()){
                et_new_phone_number.error = "Phone Number Required"
                et_new_phone_number.requestFocus()
                return@setOnClickListener
            }


//            if the password contains less than 6 characters we display error message
            if (password.isEmpty() || password.length < 6){
                et_new_password.error = "6 char password required"
                et_new_password.requestFocus()
                return@setOnClickListener
            }
            if (confirm_password != password) {
                et_new_confirm_password.error = "Must Match With Password"
            }else{

                registerUser(email, password)

            }

        }

        tv_login_instead.setOnClickListener {
            val fragment = SignIn()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.auth_frame, fragment, fragment.javaClass.simpleName)
                ?.commit()
        }
        }


    //    in this method we can create a user in firebase console
    private fun registerUser(email: String, password: String) {
        pb_sign_up.visibility = View.VISIBLE

//    using the firebase object instance we create a user in the firebase console

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    //Registration success
                    //we call the login function from the helper class
                            val user = mAuth.currentUser
                    userId = user!!.uid
                    emailAddress = user.email

                    val newUser = User(et_new_user_name.text.toString(),emailAddress.toString(),et_new_phone_number.text.toString(),gender.toString(),date_of_birth.toString(),blood_group.toString(),genotype.toString(),allergies.toString(),address.toString(),date_joined)
                    table_user.child(userId!!).setValue(newUser)
//                    Common.student_name = et_new_user_name.text.toString()
//                    Common.student_department = spinner_text.toString()
                    activity?.pb_sign_up?.visibility = View.GONE
//                    activity?.login()
                    val fragment = SignIn()
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.auth_frame, fragment, fragment.javaClass.simpleName)
                        ?.commit()
                }else{
//                we show the error message from the attempted registration
//                we set the exception message to be non nullable
                    it.exception?.message?.let {
                        activity?.pb_sign_up?.visibility = View.GONE

//                    we call the toast function from the helper class
                        activity?.toast(it)
                    }
                }
            }

            }


}
