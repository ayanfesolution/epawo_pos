package com.epawo.egole.model.fund_transfer

data class TransferFundRequest(
    var userId : Int,
    var toAccount : String,
    var toBank : String,
    var transAmount : Int,
    var transferType : String,
    var narration : String,
    var receiver : String,
    var terminalID : String,
    var pin: String,
    var mainAcctNumber: String

)
