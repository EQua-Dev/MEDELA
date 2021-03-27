package com.androidstrike.medela.adapters

import android.view.TextureView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.medela.R
import com.androidstrike.medela.interfaces.IRecyclerItemClickListener

class DrugStoreHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var txtDrgStore: TextView
    var txtDrugPrice: TextView
    var txtDrugStock: TextView


    lateinit var iRecyclerItemClickListener: IRecyclerItemClickListener

    fun setClick(iRecyclerItemClickListener: IRecyclerItemClickListener){
        this.iRecyclerItemClickListener = iRecyclerItemClickListener
    }

//    fun setClick(iRecyclerItemClickListener: IRecyclerItemClickListener) {
//        this.iRecyclerItemClickListener = iRecyclerItemClickListener
//    }

    init {
        txtDrgStore = itemView.findViewById(R.id.txtStoreName) as TextView
        txtDrugPrice = itemView.findViewById(R.id.txt_drug_price) as TextView
        txtDrugStock = itemView.findViewById(R.id.txt_drug_stock) as TextView


        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        iRecyclerItemClickListener.onItemClickListener(v!!, adapterPosition)
    }
}