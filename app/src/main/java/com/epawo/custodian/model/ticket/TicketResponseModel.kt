package com.epawo.custodian.model.ticket

data class TicketResponseModel(
    val statuCode : String,
    val message : String,
    val ticket : List<TicketData>
)
