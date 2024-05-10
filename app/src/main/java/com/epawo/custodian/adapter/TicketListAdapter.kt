package com.epawo.custodian.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.epawo.custodian.R
import com.epawo.custodian.model.ticket.TicketData
import com.epawo.custodian.utilities.Utility

class TicketListAdapter (private val context : Context, private val transList : List<TicketData>
) : RecyclerView.Adapter<TicketListAdapter.MyViewHolder>() {

    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){

        var ticketTitle : TextView
        var ticketDate : TextView
        var ticketStatus : TextView
        var ticketStatusSymbol : View

        init {
            ticketTitle = view.findViewById(R.id.ticketTitle)
            ticketDate = view.findViewById(R.id.ticketDate)
            ticketStatus = view.findViewById(R.id.ticketStatus)
            ticketStatusSymbol = view.findViewById(R.id.ticketStatusSymbol)
        }
    }

    override fun getItemCount(): Int {
        return transList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_ticket_items, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TicketListAdapter.MyViewHolder, position: Int) {
        var transaction = transList[position]
        holder.ticketTitle.text = transaction.description
        holder.ticketDate.text = Utility.convertDateToUTCDate(transaction.dateCreated)
        holder.ticketStatus.text = transaction.status
        if(transaction.status == "Resolved"){
            holder.ticketStatusSymbol.setBackgroundDrawable(context.getDrawable(R.drawable.round_corner_view_resolved))
        }else if(transaction.status == "Pending"){
            holder.ticketStatusSymbol.setBackgroundDrawable(context.getDrawable(R.drawable.round_corner_view_pending))
        }else{
            holder.ticketStatusSymbol.setBackgroundDrawable(context.getDrawable(R.drawable.round_corner_view_unread))
        }
    }
}