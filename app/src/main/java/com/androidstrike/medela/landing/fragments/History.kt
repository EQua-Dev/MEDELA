package com.androidstrike.medela.landing.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.medela.R
import com.androidstrike.medela.adapters.HistoryHolder
import com.androidstrike.medela.helpers.MyButton
import com.androidstrike.medela.helpers.MySwipeHelper
import com.androidstrike.medela.interfaces.IRecyclerItemClickListener
import com.androidstrike.medela.interfaces.MyButtonClickListener
import com.androidstrike.medela.models.Histories
import com.androidstrike.medela.utils.toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_history.*
import java.lang.StringBuilder
import java.text.NumberFormat
import java.util.*

class History : Fragment() {

    lateinit var database: FirebaseDatabase
    lateinit var queryHistory: DatabaseReference

    private var mAuth: FirebaseAuth? = null
    private var firebaseUser: FirebaseUser? = null

    var historyModel: Histories? = null

    var storeAddress: String? = null
    var storePhone: String? = null

    var adapter: FirebaseRecyclerAdapter<Histories, HistoryHolder>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        rv_history.layoutManager = layoutManager
        rv_history.addItemDecoration(DividerItemDecoration(activity, layoutManager.orientation))



        // here we initialize the instance of the Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        firebaseUser = mAuth!!.currentUser

        database = FirebaseDatabase.getInstance()
        queryHistory = database.getReference("Users/${firebaseUser?.uid}/History")

        val options = FirebaseRecyclerOptions.Builder<Histories>()
            .setQuery(queryHistory, Histories::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<Histories, HistoryHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.custom_history_layout, parent, false)

                return HistoryHolder(itemView)
            }

            override fun onBindViewHolder(holder: HistoryHolder, position: Int, model: Histories) {
                holder.txtHistDate.text = StringBuilder(model.datePurchased!!)
                holder.txtHistDrug.text = StringBuilder(model.drugName!!)
                holder.txtHistStore.text = StringBuilder(model.storeName!!)
                holder.txtHistQty.text = StringBuilder("Qty: ${model.drugQty}")

                storeAddress = model.storeAddress
                storePhone = model.storePhone

                val drugCostInt: Int = Integer.parseInt(model.drugCost)
                val locale: Locale = Locale("en", "NG")
                val fmt: NumberFormat = NumberFormat.getCurrencyInstance(locale)

                holder.txtHistPrice.text = "Cost: ${fmt.format(drugCostInt)}"

                val storeName = model.storeName

                holder.setClick(object : IRecyclerItemClickListener {
                    override fun onItemClickListener(view: View, position: Int) {
                        activity?.toast(storeName.toString())
                    }

                })
            }

        }
        adapter!!.startListening()
        rv_history.adapter = adapter
//        queryHistory.addListenerForSingleValueEvent()

        //add swipe
        val swipe = object : MySwipeHelper(requireContext(), rv_history, 200) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                //add button
                buffer.add(
                    MyButton(
                        context!!,
                        "Call",
                        30,
                        R.drawable.ic_baseline_call_24,
                        Color.parseColor("#FF3C30"),
                        object : MyButtonClickListener {
                            override fun onClick(pos: Int) {
                                val dialIntent = Intent(Intent.ACTION_CALL)
                                dialIntent.data = Uri.fromParts("tel",storePhone,null)
                                startActivity(dialIntent)
                                //call pharmacy
                            }

                        })
                )
                buffer.add(
                    MyButton(
                        context!!,
                        "Locate",
                        30,
                        R.drawable.ic_baseline_location_on_24,
                        Color.parseColor("#FF3C30"),
                        object : MyButtonClickListener {
                            override fun onClick(pos: Int) {
                                val builder = Uri.Builder()
                                builder.scheme("geo")
                                    .path("0,0")
                                    .query(storeAddress)

                                val addressUri = builder.build()

                                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=$storeAddress") )//addressUri)

                                if (mapIntent.resolveActivity(activity?.packageManager!!) !=null){
                                    startActivity(mapIntent)
                                }
                                //call pharmacy
                            }

                        })
                )
            }

        }

    }

}