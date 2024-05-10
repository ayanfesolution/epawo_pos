package com.epawo.custodian.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.epawo.custodian.R
import com.epawo.custodian.interfaces.BettingProviderClickListener
import com.epawo.custodian.model.betting.response.BettingResponseModel

class BettingProviderAdapter(private val context : Context, private val bettingProviderList : List<BettingResponseModel>,
        private val bettingListener : BettingProviderClickListener) : RecyclerView.Adapter<BettingProviderAdapter.MyViewHolder>() {

    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){

        var providerImage : ImageView
        var providerName : TextView
        var bettingWrapper : ConstraintLayout

        init {
            providerImage = view.findViewById(R.id.providerImage)
            providerName = view.findViewById(R.id.providerName)
            bettingWrapper = view.findViewById(R.id.bettingWrapper)
        }
    }

    override fun getItemCount(): Int {
        return bettingProviderList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BettingProviderAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_betting_provider_items, parent, false)
        return BettingProviderAdapter.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BettingProviderAdapter.MyViewHolder, position: Int) {
        var betting = bettingProviderList[position]
        holder.providerName.text = betting.name
        Glide.with(context)
            .load(betting.logo)
            .into(holder.providerImage)
        holder.bettingWrapper.setOnClickListener { bettingListener.onBettingProviderClicked(betting) }

    }
}