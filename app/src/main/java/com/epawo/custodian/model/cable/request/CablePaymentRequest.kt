package com.epawo.custodian.model.cable.request

data class CablePaymentRequest(
    var userID : Int,
    var productID : Int,
    var terminalID : String,
    var subscriberId : String,
    var productCode : String,
    var providerCode : String,
    var amount : Int,
    var quantity : Int,
    var accountName : String,
    var type: String,
    var addOnCode: String,
    var addonPrice: String,
    var pin: String,
    var mainAcctNumber: String,
    var surauth : String
)
