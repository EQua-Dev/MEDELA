package com.androidstrike.medela.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.medela.R
import com.androidstrike.medela.interfaces.IRecyclerItemClickListener

class DrugHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var txtDrgName: TextView
    var txtMfctName: TextView

    lateinit var iRecyclerItemClickListener: IRecyclerItemClickListener

    fun setClick(iRecyclerItemClickListener: IRecyclerItemClickListener) {
        this.iRecyclerItemClickListener = iRecyclerItemClickListener
    }

    init {
        txtDrgName = itemView.findViewById(R.id.txtNameDrug) as TextView
        txtMfctName = itemView.findViewById(R.id.txtManufacturerName) as TextView

        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        iRecyclerItemClickListener.onItemClickListener(v!!, adapterPosition)
    }
}