package com.android.warmindo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DetailAdapter(private val detailTransactionList: List<DetailModel>) :
    RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaMenu: TextView = itemView.findViewById(R.id.namapelanggan)
        val jumlah: TextView = itemView.findViewById(R.id.jumlah)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDetailTransaction = detailTransactionList[position]

        holder.namaMenu.text = currentDetailTransaction.namamenu
        holder.jumlah.text = "Jumlah: ${currentDetailTransaction.jumlah}"
    }

    override fun getItemCount(): Int {
        return detailTransactionList.size
    }
}
