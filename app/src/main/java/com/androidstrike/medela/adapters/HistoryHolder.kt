package com.androidstrike.medela.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.medela.R
import com.androidstrike.medela.interfaces.IRecyclerItemClickListener
import kotlinx.android.synthetic.main.custom_history_layout.view.*

class HistoryHolder (itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var txtHistDate: TextView
    var txtHistDrug: TextView
    var txtHistStore: TextView
    var txtHistQty: TextView
    var txtHistPrice: TextView

    lateinit var iRecyclerItemClickListener: IRecyclerItemClickListener

    fun setClick(iRecyclerItemClickListener: IRecyclerItemClickListener) {
        this.iRecyclerItemClickListener = iRecyclerItemClickListener
    }

    init {
        txtHistDate = itemView.findViewById(R.id.cstm_hist_date) as TextView
        txtHistDrug = itemView.findViewById(R.id.cstm_hist_drug) as TextView
        txtHistStore = itemView.findViewById(R.id.cstm_hist_store) as TextView
        txtHistQty = itemView.findViewById(R.id.cstm_hist_qty) as TextView
        txtHistPrice = itemView.findViewById(R.id.cstm_hist_price) as TextView

        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        iRecyclerItemClickListener.onItemClickListener(v!!, adapterPosition)
    }
}