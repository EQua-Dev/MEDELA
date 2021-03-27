package com.androidstrike.medela.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidstrike.medela.R
import com.androidstrike.medela.adapters.DrugStoreHolder
import com.androidstrike.medela.interfaces.IRecyclerItemClickListener
import com.androidstrike.medela.models.DrugStore
import com.androidstrike.medela.models.Drugs
import com.androidstrike.medela.utils.toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_drug.*
import java.lang.StringBuilder
import java.text.NumberFormat
import java.util.*

class Drug : Fragment() {

    lateinit var database: FirebaseDatabase
    lateinit var query: DatabaseReference
    lateinit var queryDrugStore: DatabaseReference

    var drugModel: Drugs? = null

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


        if (arguments?.getString("drugId") != null){
            dDrugId = arguments?.getString("drugId")
        }

        database = FirebaseDatabase.getInstance()
        query = database.getReference("Drugs/$dDrugId")

        val drugListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                drugModel = snapshot.getValue(Drugs::class.java)

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

        adapter = object : FirebaseRecyclerAdapter<DrugStore, DrugStoreHolder>(options){
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


                holder.setClick(object : IRecyclerItemClickListener {
                    override fun onItemClickListener(view: View, position: Int) {

                        activity?.toast("$storeId clicked!")

                        val frag_store = Store()

                        val bundle = Bundle()
                        bundle.putString("storeId", storeId)

                        val manager = fragmentManager
                        val frag_transaction = manager?.beginTransaction()
                        frag_transaction?.replace(R.id.fragment_container, frag_store)
                        frag_transaction?.commit()
                    }
                })

            }


        }

        adapter!!.startListening()
        rv_drug_stores.adapter = adapter
        query.addListenerForSingleValueEvent(drugListener)
//        txtDrug.text = dDrugId
    }

}