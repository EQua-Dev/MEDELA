package com.androidstrike.medela.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidstrike.medela.R
import com.androidstrike.medela.adapters.DrugStoreHolder
import com.androidstrike.medela.interfaces.IRecyclerItemClickListener
import com.androidstrike.medela.models.DrugStore
import com.androidstrike.medela.models.Drugs
import com.androidstrike.medela.models.StoreDrug
import com.androidstrike.medela.models.Stores
import com.androidstrike.medela.utils.toast
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_store_purchase.*
import kotlinx.android.synthetic.main.fragment_drug.*
import java.lang.StringBuilder
import java.text.NumberFormat
import java.util.*

class Drug : Fragment() {

    lateinit var database: FirebaseDatabase
    lateinit var query: DatabaseReference
    lateinit var queryDrugStore: DatabaseReference
    lateinit var queryDrugStoreDetail: DatabaseReference
    lateinit var queryStoreDrugDetail: DatabaseReference

    lateinit var theDrugName: String

    var newQuantityValue: Int = 0

    var drugModel: Drugs? = null
    var storeModel: Stores? = null
    var storeDrugModel: StoreDrug? = null

    var dDrugId: String? = null

    var drugPrice: Int = 0

    var adapter: FirebaseRecyclerAdapter<DrugStore, DrugStoreHolder>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drug, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        rv_drug_stores.layoutManager = layoutManager
        rv_drug_stores.addItemDecoration(DividerItemDecoration(activity, layoutManager.orientation))


        if (arguments?.getString("drugId") != null) {
            dDrugId = arguments?.getString("drugId")
        }

        database = FirebaseDatabase.getInstance()
        query = database.getReference("Drugs/$dDrugId")

        val drugListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                drugModel = snapshot.getValue(Drugs::class.java)

                theDrugName = drugModel!!.name

                txt_drug_name.text = drugModel!!.name
                txt_drug_manufactured.text = drugModel!!.manufacturer
                txt_adult_dosage.text = drugModel!!.adult
                txt_children_dosage.text = drugModel!!.children

                Picasso.get().load(drugModel!!.image)
                        .into(iv_drug)

            }

            override fun onCancelled(error: DatabaseError) {
                activity?.toast(error.message)
            }

        }

        queryDrugStore = database.getReference("Drugs/$dDrugId/stores")

        val options = FirebaseRecyclerOptions.Builder<DrugStore>()
                .setQuery(queryDrugStore, DrugStore::class.java)
                .build()

        adapter = object : FirebaseRecyclerAdapter<DrugStore, DrugStoreHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugStoreHolder {
                val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_drug_store, parent, false)

                return DrugStoreHolder(itemView)
            }

            override fun onBindViewHolder(holder: DrugStoreHolder, position: Int, model: DrugStore) {

                holder.txtDrgStore.text = StringBuilder(model.name)

                drugPrice = Integer.parseInt(model.price)   //as Int

                val locale: Locale = Locale("en", "NG")
                val fmt: NumberFormat = NumberFormat.getCurrencyInstance(locale)

                holder.txtDrugPrice.append(fmt.format(drugPrice).toString())
                holder.txtDrugStock.append("${model.stock} Left")


                val storeId = model.id
                val stockLeft = model.stock
                val stockDrugPrice = drugPrice
//                val drugName = theDrugName


                holder.setClick(object : IRecyclerItemClickListener {
                    override fun onItemClickListener(view: View, position: Int) {

                        activity?.toast("$storeId clicked!")
                        if (stockLeft == "1")
                            activity?.toast("Out of Stock!")
                        else {
                            showStoreDialog(storeId, stockDrugPrice)

//                            val newStock = stockLeft.toInt() - newQuantityValue
//                            queryDrugStore.child("/$storeId/stock").setValue(newStock)
                        }




//                        val frag_store = Store()
//
//                        val bundle = Bundle()
//                        bundle.putString("storeId", storeId)
//
//                        val manager = fragmentManager
//                        val frag_transaction = manager?.beginTransaction()
//                        frag_transaction?.replace(R.id.fragment_container, frag_store)
//                        frag_transaction?.commit()
                    }
                })

            }


        }

        adapter!!.startListening()
        rv_drug_stores.adapter = adapter
        query.addListenerForSingleValueEvent(drugListener)
//        txtDrug.text = dDrugId
    }

    private fun showStoreDialog(storeId: String, drugPrice: Int) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.drawable.custom_design_dialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_store_purchase)

        val customStoreName = dialog.findViewById(R.id.custom_store_name) as TextView
        val customStoreAddress = dialog.findViewById(R.id.custom_store_address) as TextView
        val customStoreHours = dialog.findViewById<TextView>(R.id.custom_store_hours)
        val customStorePhone = dialog.findViewById<TextView>(R.id.custom_store_phone)
        val customDrugName = dialog.findViewById<TextView>(R.id.custom_drug_name)
        val customDrugPrice = dialog.findViewById<TextView>(R.id.custom_drug_price)
        val customBtnCancel = dialog.findViewById<Button>(R.id.btn_drug_cancel)
        val customBtnPurchase = dialog.findViewById<Button>(R.id.btn_drug_purchase)
        val qtyButton = dialog.findViewById<ElegantNumberButton>(R.id.number_button)
        var price = drugPrice
        val locale: Locale = Locale("en", "NG")
        val fmt: NumberFormat = NumberFormat.getCurrencyInstance(locale)

        var drugName: String? = null
        var drugTotalPrice: Int = price
        var drugQty: String? = null
        var storeName: String? = null
        var drugStorePhone: String? = null
        var drugStoreAddress: String? = null

        customDrugPrice.text = "Price: ${fmt.format(price)}"

        customDrugName.text = dDrugId

        queryDrugStoreDetail = database.getReference("Stores/$storeId")

        val drugDetailListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                storeModel = snapshot.getValue(Stores::class.java)

                customStoreName.text = storeModel!!.storename
                customStoreAddress.text = storeModel!!.address
                customStoreHours.text = storeModel!!.active
                customStorePhone.text = storeModel!!.phone

                drugName = dDrugId

                drugStoreAddress = storeModel!!.address
                drugStorePhone = storeModel!!.phone

                storeName = storeModel!!.storename

            }

            override fun onCancelled(error: DatabaseError) {
                activity?.toast(error.message)
            }

        }

        qtyButton.setOnValueChangeListener { view, oldValue, newValue ->

            newQuantityValue = newValue
            val newPrice = price * newValue
            customDrugPrice.text = "Price: ${fmt.format(newPrice)}"

            drugQty = newValue.toString()
            drugTotalPrice = newPrice



        }

        queryDrugStoreDetail.addListenerForSingleValueEvent(drugDetailListener)

        customBtnPurchase.setOnClickListener {
            activity?.toast("Purchasing...")

            val frag_rave = Flutterwave()

            val bundle = Bundle()
            bundle.putString("drugStoreAddress", drugStoreAddress)
            bundle.putString("drugStorePhone", drugStorePhone)
            bundle.putString("drugName", drugName)
            bundle.putInt("drugTotalPrice", drugTotalPrice)
            bundle.putString("drugQtyTotal", drugQty)
            bundle.putString("storeName", storeName)
            frag_rave.arguments = bundle

            val manager = fragmentManager

            val frag_transaction = manager?.beginTransaction()

            frag_transaction?.replace(R.id.fragment_container, frag_rave)
            frag_transaction?.commit()


            dialog.dismiss()
        }
        customBtnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}