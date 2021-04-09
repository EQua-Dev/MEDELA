package com.androidstrike.medela.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidstrike.medela.R
import com.androidstrike.medela.landing.fragments.History
import com.androidstrike.medela.models.Histories
import com.androidstrike.medela.utils.Common
import com.androidstrike.medela.utils.toast
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class Flutterwave : Fragment() {
//  bundle.putString("drugName", drugName)
//            bundle.putInt("drugTotalPrice", drugTotalPrice)
//            bundle.putString("drugQtyTotal", drugQty)
//            bundle.putString("storeName", storeName)

//    drugStoreAddress", drugStoreAddress)
//    bundle.putString("drugStorePhone"
    var addressOfStore: String? = null
    var phoneOfStore: String? = null
    var nameOfDrug: String? = null
    var totalPriceOfDrug: Int? = null
    var totalQtyOfDrug: String? = null
    var nameOfStore: String? = null

    val publicKey = "FLWPUBK-2d6ccddd437ac5c89668bb3116f33ed5-X"
    val encrypyKey = "884f53bff48591fe29facb22"

    private lateinit var mAuth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var table_history: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flutterwave, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments?.getInt("drugTotalPrice") != null){
            nameOfDrug = arguments?.getString("drugName")
            totalPriceOfDrug = arguments?.getInt("drugTotalPrice")
            totalQtyOfDrug = arguments?.getString("drugQtyTotal")
            nameOfStore = arguments?.getString("storeName")
            addressOfStore = arguments?.getString("drugStoreAddress")
            phoneOfStore = arguments?.getString("drugStorePhone")
        }

  // here we initialize the instance of the Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val user = mAuth.currentUser?.uid
        table_history = database.getReference().child("Users/$user/History/${System.currentTimeMillis()}")

        makePayment()

    }

    private fun makePayment() {
        RaveUiManager(this)
            .setAmount(totalPriceOfDrug?.toDouble()!!)
            .setEmail(Common.currentUser.email)
            .setCountry("NG")
            .setCurrency("NGN").setfName(Common.user_name)
            .setNarration("Payment for $nameOfDrug")
            .setPublicKey(publicKey)
            .setEncryptionKey(encrypyKey)
            .setTxRef("${System.currentTimeMillis()} Ref")
            .acceptAccountPayments(true)
            .acceptCardPayments(true)
            .onStagingEnv(true)
            .shouldDisplayFee(true)
            .showStagingLabel(true)
            .initialize()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       if (requestCode === RAVE_REQUEST_CODE && data != null){
           val message: String = data.getStringExtra("response").toString() //returns the entire raw data of the transaction
           Log.d("EQUA", "onActivityResult: $message")
           if (resultCode === RavePayActivity.RESULT_SUCCESS){
               activity?.toast("Payment Succesful")

               val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
               val currentDate = sdf.format(Date())


               val newHistory = Histories(nameOfDrug, totalQtyOfDrug, totalPriceOfDrug.toString(), nameOfStore, currentDate,addressOfStore,phoneOfStore)
               table_history.setValue(newHistory)

               val frag_rave = History()

               val manager = fragmentManager

               val frag_transaction = manager?.beginTransaction()

               frag_transaction?.replace(R.id.fragment_container, frag_rave)
               frag_transaction?.commit()
           }
           else if (resultCode === RavePayActivity.RESULT_ERROR) {
               activity?.toast("ERROR $message")

           } else if (resultCode === RavePayActivity.RESULT_CANCELLED) {
               activity?.toast("CANCELLED $message")

           }
       }else{
           super.onActivityResult(requestCode, resultCode, data)
       }
    }
}