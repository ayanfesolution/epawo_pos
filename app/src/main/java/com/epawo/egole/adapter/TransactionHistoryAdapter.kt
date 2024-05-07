package com.epawo.egole.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.epawo.egole.R
import com.epawo.egole.interfaces.TransactionClickListener
import com.epawo.egole.model.transaction.response.Transactions
import com.epawo.egole.utilities.AppConstants
import com.epawo.egole.utilities.AppConstants.NAIRA_SIGN
import com.epawo.egole.utilities.Utility

class TransactionHistoryAdapter(private val transList :  List<Transactions>,private val listener : TransactionClickListener,
    private val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.layout_transaction_items, parent, false)
            ItemViewHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.layout_item_loading, parent, false)
            LoadingViewHolder(view)
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            populateItemRows(holder, position)
        } else if (holder is LoadingViewHolder) {
            showLoadingView(holder, position)
        }
    }

    override fun getItemCount(): Int {
        return transList.size
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_ITEM
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var transactionMethod: TextView
        var transactionStatus : TextView
        var transactionAmount : TextView
        var transactionDate : TextView
        var transItemWrapper : ConstraintLayout

        init {
            transactionMethod = itemView.findViewById(R.id.transactionMethod)
            transactionStatus = itemView.findViewById(R.id.transactionStatus)
            transactionAmount = itemView.findViewById(R.id.transactionAmount)
            transactionDate = itemView.findViewById(R.id.transactionDate)
            transItemWrapper = itemView.findViewById(R.id.trans_item_wrapper)
        }
    }

    class LoadingViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private var progressBar: ProgressBar

        init {
            progressBar = itemView.findViewById(R.id.progressbar)
        }
    }

    private fun populateItemRows(viewHolder: ItemViewHolder, position: Int) {
        val items = transList[position]
        val transactionStatus = items.transactionStatus.toString()
        if(transactionStatus.contains("Successful") || transactionStatus.contains("successful") ){
            viewHolder.transactionStatus.setTextColor(context.resources.getColor(R.color.app_green))
        }else{
            viewHolder.transactionStatus.setTextColor(context.resources.getColor(R.color.app_color))
        }

        viewHolder.transactionMethod.text = items.transactionMethod
        viewHolder.transactionStatus.text = transactionStatus
        viewHolder.transactionAmount.text = NAIRA_SIGN + items.amount.toString()
        viewHolder.transactionDate.text = Utility.convertDateToUTCDates(items.transactionDate.toString())
        viewHolder.transItemWrapper.setOnClickListener {
            listener.onTransactionClicked(items)
        }
    }

    private fun showLoadingView(viewHolder: LoadingViewHolder, position: Int) {
        // Progressbar would be displayed
    }
}