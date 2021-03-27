package com.androidstrike.medela.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidstrike.medela.R
import kotlinx.android.synthetic.main.fragment_store.*

class Store : Fragment() {

    var sStoreId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments?.getString("storeId") != null){
            sStoreId = arguments?.getString("storeId")
        }

        store_name.text = sStoreId
    }
}