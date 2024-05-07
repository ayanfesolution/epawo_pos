package com.epawo.egole.model.ticket

data class TicketResponseModel(
    val statuCode : String,
    val message : String,
    val ticket : List<TicketData>
)
