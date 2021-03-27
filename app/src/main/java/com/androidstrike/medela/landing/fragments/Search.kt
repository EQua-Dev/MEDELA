package com.androidstrike.medela.landing.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidstrike.medela.R
import com.androidstrike.medela.adapters.DrugHolder
import com.androidstrike.medela.fragments.Drug
import com.androidstrike.medela.interfaces.IFirebaseLoadDone
import com.androidstrike.medela.interfaces.IRecyclerItemClickListener
import com.androidstrike.medela.models.Drugs
import com.androidstrike.medela.utils.toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.fragment_search.*
import java.lang.StringBuilder

class Search : Fragment(), IFirebaseLoadDone {

    var adapter: FirebaseRecyclerAdapter<Drugs, DrugHolder>? = null
    var searchAdapter: FirebaseRecyclerAdapter<Drugs, DrugHolder>? = null

    lateinit var database: FirebaseDatabase
    lateinit var drugList: DatabaseReference

    lateinit var iFirebaseLoadDone: IFirebaseLoadDone
    var suggestList: MutableList<String> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        database = FirebaseDatabase.getInstance()
        drugList = database.getReference("Drugs")

        //Init View
        //For search bar
        loadSuggest()
        material_search_bar_drugs.lastSuggestions = suggestList
        material_search_bar_drugs.setCardViewElevation(5)
        material_search_bar_drugs.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var suggest = ArrayList<String>()
                for (search in suggestList){
                    if (search.toLowerCase().contentEquals(material_search_bar_drugs.text.toLowerCase()))
                        suggest.add(search)
                }
                material_search_bar_drugs.lastSuggestions = suggest

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        material_search_bar_drugs.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener{
            override fun onSearchStateChanged(enabled: Boolean) {
                if (!enabled){
                    if (adapter != null)
                        recycler_all_drugs.adapter = adapter
                }
            }

            override fun onSearchConfirmed(text: CharSequence?) {
                startSearch(text.toString())
                material_search_bar_drugs.clearSuggestions()
            }

            override fun onButtonClicked(buttonCode: Int) {

            }

        })


        recycler_all_drugs.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        recycler_all_drugs.layoutManager = layoutManager
        recycler_all_drugs.addItemDecoration(
                DividerItemDecoration(
                        activity,
                        layoutManager.orientation
                )
        )

        iFirebaseLoadDone = this

        loadUserList()
        loadSearchData()
    }

    private fun loadSuggest() {
        drugList.orderByChild("name")
                .addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (postSnapshot in snapshot.children){
                            val item = postSnapshot.getValue(Drugs::class.java)
                            suggestList.add(item?.name!!)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
    }

    private fun startSearch(search_string: String) {
        val query = FirebaseDatabase.getInstance()
                .getReference("Drugs")
                .orderByChild("name")
                .startAt(search_string)

        val options =
                FirebaseRecyclerOptions.Builder<Drugs>()
                        .setQuery(query, Drugs::class.java)
                        .build()

        searchAdapter = object: FirebaseRecyclerAdapter<Drugs, DrugHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugHolder {
                val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_drug, parent, false)
                return DrugHolder(itemView)
            }

            override fun onBindViewHolder(holder: DrugHolder,
                                          position: Int,
                                          model: Drugs) {

                holder.txtDrgName.text = StringBuilder(model.name)
                holder.txtMfctName.text = StringBuilder(model.manufacturer)

                holder.setClick(object : IRecyclerItemClickListener{
                    override fun onItemClickListener(view: View, position: Int) {
                        val drugId = model.name

                        val frag_drug = Drug()

                        val bundle = Bundle()
                        bundle.putString("drugId", drugId)
                        frag_drug.arguments = bundle

                        val manager = fragmentManager
                        val frag_trans = manager?.beginTransaction()
                        frag_trans?.replace(R.id.fragment_container, frag_drug)
                        frag_trans?.commit()
                    }

                })
            }

        }
        searchAdapter!!.startListening()
        recycler_all_drugs.adapter = searchAdapter
    }

    private fun loadSearchData() {
        val lstDrugName = ArrayList<String>()
        val drugList = FirebaseDatabase.getInstance().getReference("Drugs")
        drugList.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (drugSnapshot in snapshot.children){
                    val drug = drugSnapshot.getValue(Drugs::class.java)!!
                    lstDrugName.add(drug!!.name!!)
                }
                iFirebaseLoadDone.onFirebaseUserDone(lstDrugName)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun loadUserList() {
        val query = FirebaseDatabase.getInstance().getReference("Drugs")

        val options = FirebaseRecyclerOptions.Builder<Drugs>()
                .setQuery(query, Drugs::class.java)
                .build()

        adapter = object : FirebaseRecyclerAdapter<Drugs, DrugHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugHolder {
                val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_drug, parent, false)
                return DrugHolder(itemView)
            }

            override fun onBindViewHolder(holder: DrugHolder, position: Int, model: Drugs) {
                holder.txtDrgName.text = StringBuilder(model.name)
                holder.txtMfctName.text = StringBuilder(model.manufacturer)

                holder.setClick(object : IRecyclerItemClickListener{
                    override fun onItemClickListener(view: View, position: Int) {
                        val drugId = model.name

                        val frag_drug = Drug()

                        val bundle = Bundle()
                        bundle.putString("drugId", drugId)
                        frag_drug.arguments = bundle

                        val manager = fragmentManager
                        val frag_trans = manager?.beginTransaction()
                        frag_trans?.replace(R.id.fragment_container, frag_drug)
                        frag_trans?.commit()
                    }

                })
            }

        }

        adapter!!.startListening()
        recycler_all_drugs.adapter = adapter
    }


    override fun onStop() {
        if (adapter != null)
            adapter!!.stopListening()
        if (searchAdapter != null)
            searchAdapter!!.stopListening()

        super.onStop()
    }

    override fun onFirebaseUserDone(lstEmail: List<String>) {
        material_search_bar_drugs.lastSuggestions = lstEmail
    }

    override fun onFirebaseLoadFailed(message: String) {
        activity?.toast(message)
    }
}