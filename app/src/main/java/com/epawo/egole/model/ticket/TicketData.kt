package com.epawo.egole.model.ticket

data class TicketData(
    var referenceNo : String,
    var subject : String,
    var description : String,
    var walletID : Int,
    var walletInfo : String,
    var receivedBy : Int,
    var treatedBy : Int,
    var receivedByInfo : String? = "",
    var treatedByInfo : String? = "",
    var dateCreated : String,
    var status : String,
    var ticketDetail : String? = ""
)
