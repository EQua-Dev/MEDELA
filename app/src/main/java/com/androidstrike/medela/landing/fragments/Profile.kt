package com.androidstrike.medela.landing.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.androidstrike.medela.R
import com.androidstrike.medela.models.User
import com.androidstrike.medela.utils.Common
import com.androidstrike.medela.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Profile : Fragment(), DatePickerDialog.OnDateSetListener {

    lateinit var database: FirebaseDatabase
    lateinit var query: DatabaseReference

    private var mAuth: FirebaseAuth? = null
    private var firebaseUser: FirebaseUser? = null

    lateinit var calendar: Calendar

    var usrModel: User? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        firebaseUser = mAuth!!.currentUser

        database = FirebaseDatabase.getInstance()
        query = database.getReference("Users/${firebaseUser?.uid}")


        val profileListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usrModel = snapshot.getValue(User::class.java)

//                Common.user_name = usrModel!!.name

                profile_name.text = usrModel!!.name
                if (usrModel!!.gender == "null")
                    profile_gender.text = "Click to update!"
                else
                profile_gender.text = usrModel!!.gender
                if (usrModel!!.dateOfBirth == "null")
                    profile_dob.text = "Click to update!"
                    else
                profile_dob.text = usrModel!!.dateOfBirth
                if (usrModel!!.blood_group == "null")
                    profile_bld_grp.text = "Click to update!"
                    else
                profile_bld_grp.text = usrModel!!.blood_group
                if (usrModel!!.genotype == "null")
                    profile_genotype.text = "Click to update!"
                    else
                profile_genotype.text = usrModel!!.genotype
                if (usrModel!!.address == "null")
                    profile_address.text = "Click to update!"
                    else
                profile_address.text = usrModel!!.address
                if (usrModel!!.phone == "null")
                    profile_phone.text = "Click to update!"
                    else
                profile_phone.text = usrModel!!.phone
                profile_email.text = usrModel!!.email
//                profile_date_joined.append("Date Joined: ${usrModel!!.date_joined}")

                setClicks()

            }

            override fun onCancelled(error: DatabaseError) {
                activity?.toast(error.message)
            }

        }
        query.addListenerForSingleValueEvent(profileListener)

    }


    var checkedGender = -1
    var checkedBloodGroup = -1
    var checkedGenotype = -1

    var day = 0
    var month: Int = 0
    var year: Int = 0

    var birthDay = 0
    var birthMonth: Int = 0
    var birthYear: Int = 0

    lateinit var dateOfBirth: String

    private fun setClicks() {

        profile_name.setOnClickListener {
            // edittext alert dialog
        }
        profile_gender.setOnClickListener {
            // radio button dialog
            val genders = arrayOf("Male", "Female")
            lateinit var selectedGender: String

            val genderBuilder = AlertDialog.Builder(context)
            genderBuilder.setTitle("Select Gender")
            genderBuilder.setSingleChoiceItems(
                    genders,
                    checkedGender,
                    DialogInterface.OnClickListener { dialog, which ->
                        checkedGender = which
                        for (gender in genders) {
                            when (checkedGender) {
                                0 -> {
                                    selectedGender = "Male"
//                                    usrModel!!.gender = selectedGender
                                }
                                1 -> {
                                    selectedGender = "Female"
//                                    usrModel!!.gender = selectedGender
                                }
                            }
                        }
                    }
            )
            genderBuilder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                query.child("gender").setValue(selectedGender)
                profile_gender.text = selectedGender
            })
            genderBuilder.setNegativeButton("Cancel", null)
            genderBuilder.show()
        }
        profile_dob.setOnClickListener {
            // date picker
            calendar = Calendar.getInstance()

            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(requireContext(), android.R.style.Theme_Holo_Dialog, this, year, month, day)
            datePickerDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
            // Set dialog icon and title.
            datePickerDialog.setIcon(R.drawable.ic_baseline_child_care_profile24);
            datePickerDialog.setTitle("Select Birth Date")
            datePickerDialog.show()
        }
        profile_bld_grp.setOnClickListener {
            // radio button dialog
            val bloodGroups = arrayOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
            lateinit var selectedBloodGroup: String

            val genderBuilder = AlertDialog.Builder(context)
            genderBuilder.setTitle("Select Blood Group")
            genderBuilder.setSingleChoiceItems(
                    bloodGroups,
                    checkedBloodGroup,
                    DialogInterface.OnClickListener { dialog, which ->
                        checkedBloodGroup = which
                        for (gender in bloodGroups) {
                            when (checkedBloodGroup) {
                                0 -> {
                                    selectedBloodGroup = "A+"
//                                    usrModel!!.gender = selectedGender
                                }
                                1 -> {
                                    selectedBloodGroup = "A-"
//                                    usrModel!!.gender = selectedGender
                                }
                                2 -> {
                                    selectedBloodGroup = "B+"
                                }
                                3 -> {
                                    selectedBloodGroup = "B-"
                                }
                                4 -> {
                                    selectedBloodGroup = "O+"
                                }
                                5 -> {
                                    selectedBloodGroup = "O-"
                                }
                                6 -> {
                                    selectedBloodGroup = "AB+"
                                }
                                7 -> {
                                    selectedBloodGroup = "AB-"
                                }
                            }
                        }
                    }
            )
            genderBuilder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                query.child("blood_group").setValue(selectedBloodGroup)
                profile_bld_grp.text = selectedBloodGroup

            })
            genderBuilder.setNegativeButton("Cancel", null)
            genderBuilder.show()
        }
        profile_genotype.setOnClickListener {
            // radio button dialog
            val genotypes = arrayOf("AA", "AO", "BB", "BO", "AB", "OO")
            lateinit var selectedGenotype: String

            val genotypeBuilder = AlertDialog.Builder(context)
            genotypeBuilder.setTitle("Select Gender")
            genotypeBuilder.setSingleChoiceItems(
                    genotypes,
                    checkedGenotype,
                    DialogInterface.OnClickListener { dialog, which ->
                        checkedGenotype = which
                        for (gender in genotypes) {
                            when (checkedGenotype) {
                                0 -> {
                                    selectedGenotype = "AA"
//                                    usrModel!!.gender = selectedGender
                                }
                                1 -> {
                                    selectedGenotype = "AO"
//                                    usrModel!!.gender = selectedGender
                                }
                                2 -> {
                                    selectedGenotype = "BB"
                                }
                                3 -> {
                                    selectedGenotype = "BO"
                                }
                                4 -> {
                                    selectedGenotype = "AB"
                                }
                                5 -> {
                                    selectedGenotype = "OO"
                                }
                            }
                        }
                    }
            )
            genotypeBuilder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                query.child("genotype").setValue(selectedGenotype)
                profile_genotype.text = selectedGenotype
            })
            genotypeBuilder.setNegativeButton("Cancel", null)
            genotypeBuilder.show()
        }
        profile_allergies.setOnClickListener {
            // edittext alert dialog
            val etBuilder = MaterialAlertDialogBuilder(requireContext())

            etBuilder.setTitle("Enter Allergies separating with commas(,) : ")

            val constraintLayout = getEditTextLayout(requireContext())
            etBuilder.setView(constraintLayout)

            val textInputLayout = constraintLayout.
            findViewWithTag<TextInputLayout>("textInputLayoutTag")
            val textInputEditText = constraintLayout.
            findViewWithTag<TextInputEditText>("textInputEditTextTag")

            // alert dialog positive button
            etBuilder.setPositiveButton("Submit"){dialog,which->
                val name: String = textInputEditText.text.toString()
                query.child("allergies").setValue(name)
                profile_allergies.text = name

            }

            // alert dialog other buttons
            etBuilder.setNegativeButton("No",null)
            etBuilder.setNeutralButton("Cancel",null)

            // set dialog non cancelable
            etBuilder.setCancelable(false)

            // finally, create the alert dialog and show it
            val dialog = etBuilder.create()

            dialog.show()

            // initially disable the positive button
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

            // edit text text change listener
            textInputEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int,
                                               p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int,
                                           p2: Int, p3: Int) {
                    if (p0.isNullOrBlank()){
                        textInputLayout.error = "Name is required."
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                .isEnabled = false
                    }else{
                        textInputLayout.error = ""
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                .isEnabled = true
                    }
                }
            })
        }
        profile_address.setOnClickListener {
            // edittext alert dialog
            val etBuilder = MaterialAlertDialogBuilder(requireContext())

            etBuilder.setTitle("Enter Home Address: ")

            val constraintLayout = getEditTextLayout(requireContext())
            etBuilder.setView(constraintLayout)

            val textInputLayout = constraintLayout.
            findViewWithTag<TextInputLayout>("textInputLayoutTag")
            val textInputEditText = constraintLayout.
            findViewWithTag<TextInputEditText>("textInputEditTextTag")

            // alert dialog positive button
            etBuilder.setPositiveButton("Submit"){dialog,which->
                val name: String = textInputEditText.text.toString()
                query.child("address").setValue(name)
                profile_address.text = name

            }

            // alert dialog other buttons
            etBuilder.setNegativeButton("No",null)
            etBuilder.setNeutralButton("Cancel",null)

            // set dialog non cancelable
            etBuilder.setCancelable(false)

            // finally, create the alert dialog and show it
            val dialog = etBuilder.create()

            dialog.show()

            // initially disable the positive button
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

            // edit text text change listener
            textInputEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int,
                                               p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int,
                                           p2: Int, p3: Int) {
                    if (p0.isNullOrBlank()){
                        textInputLayout.error = "Name is required."
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                .isEnabled = false
                    }else{
                        textInputLayout.error = ""
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                .isEnabled = true
                    }
                }
            })
        }

        profile_phone.setOnClickListener {
            // ccp edittext dialog
        }
//        profile_email.setOnClickListener {
//
//        }

    }

    private fun getEditTextLayout(context: Context): ConstraintLayout {
        val constraintLayout = ConstraintLayout(context)
        val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        constraintLayout.layoutParams = layoutParams
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            constraintLayout.id = View.generateViewId()
        }

        val textInputLayout = TextInputLayout(context)
        textInputLayout.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
        layoutParams.setMargins(
                32.toDp(context),
                8.toDp(context),
                32.toDp(context),
                8.toDp(context)
        )
        textInputLayout.layoutParams = layoutParams
        textInputLayout.hint = "Input name"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textInputLayout.id = View.generateViewId()
        }
        textInputLayout.tag = "textInputLayoutTag"


        val textInputEditText = TextInputEditText(context)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textInputEditText.id = View.generateViewId()
        }
        textInputEditText.tag = "textInputEditTextTag"

        textInputLayout.addView(textInputEditText)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        constraintLayout.addView(textInputLayout)
        return constraintLayout
    }


    // extension method to convert pixels to dp
    fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),context.resources.displayMetrics
    ).toInt()

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

//        val monthStr: String = calendar.getDisplayName(month + 1, Calendar.LONG, Locale.getDefault())

        val strBuf = StringBuffer()
        strBuf.append(dayOfMonth)
        strBuf.append("/")
        strBuf.append(month)
        strBuf.append("/")
        strBuf.append(year)




//
//        birthDay = day
//        birthMonth = month+1
//        birthYear = year
         dateOfBirth = "$strBuf"

        query.child("dateOfBirth").setValue(dateOfBirth)
        profile_dob.text = dateOfBirth
    }
}