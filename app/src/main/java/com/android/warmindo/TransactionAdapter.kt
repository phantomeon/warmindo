package com.android.warmindo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private val transactionList: ArrayList<TransactionModel>) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaPengguna: TextView = itemView.findViewById(R.id.namapelanggan)
        val kodeMeja: TextView = itemView.findViewById(R.id.kodemeja)
        val tanggal: TextView = itemView.findViewById(R.id.tanggal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTransaction = transactionList[position]

        holder.namaPengguna.text = currentTransaction.namapelanggan
        holder.kodeMeja.text = currentTransaction.kodemeja
        holder.tanggal.text = currentTransaction.tanggal

        holder.itemView.setOnClickListener {
            val detailIntent = Intent(holder.itemView.context, DetailActivity::class.java)
            detailIntent.putExtra(DetailActivity.KEY_NAMA_PELANGGAN, currentTransaction.namapelanggan)
            detailIntent.putExtra(DetailActivity.KEY_KODE_MEJA, currentTransaction.kodemeja)
            detailIntent.putExtra(DetailActivity.KEY_TANGGAL, currentTransaction.tanggal)
            detailIntent.putExtra(DetailActivity.KEY_TRANSACTION_ID, currentTransaction.idtransaksi)

            holder.itemView.context.startActivity(detailIntent)
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}
