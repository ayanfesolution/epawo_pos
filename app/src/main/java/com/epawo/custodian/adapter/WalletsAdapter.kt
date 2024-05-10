package com.epawo.custodian.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.epawo.custodian.R
import com.epawo.custodian.interfaces.WalletClickListener
import com.epawo.custodian.model.login.response.Wallets

class WalletsAdapter(private val walletListener : WalletClickListener, private val walletList : List<Wallets>
) : RecyclerView.Adapter<WalletsAdapter.MyViewHolder>() {

    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){

        var bankName : TextView
        var accountNumber : TextView
        var walletContainer : ConstraintLayout

        init {
            bankName = view.findViewById(R.id.bankName)
            accountNumber = view.findViewById(R.id.accountNumber)
            walletContainer = view.findViewById(R.id.walletContainer)
        }
    }

    override fun getItemCount(): Int {
        return walletList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletsAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.account_type_list, parent, false)
        return WalletsAdapter.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WalletsAdapter.MyViewHolder, position: Int) {
        var transaction = walletList[position]
        holder.bankName.text = transaction.bankName
        holder.accountNumber.text = transaction.accountNumber
        holder.walletContainer.setOnClickListener { walletListener.onWalletClicked(transaction) }

    }
}