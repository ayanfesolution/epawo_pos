package com.epawo.custodian.model.airtime.request

data class AirtimeDataRequest(
    var type : String,
    var surauth : String,
    var phone : String,
    var amount : String,
    var bundle : String,
    var network : String,
    var productID : Int,
    var productModeID : Int,
    var userID : Int,
    var terminalID : String,
    var pin: String,
    var mainAcctNumber: String
)
