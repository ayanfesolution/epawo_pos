package com.epawo.custodian.model.cashout

data class CashoutResponseModel(
    var result : CashoutResponseResult,
    var statusCode : Int,
    var content : CashoutResponseContent
)

data class CashoutResponseResult(
   var message : String,
   var status : Int,
   var details : String? = ""
)

data class  CashoutResponseContent(
    var description : String,
    var statusMessageObject : StatusMessageObject,
    var statusCode : String,
    var responseCode : String? = "",
    var responseMessage : String? = "",
    var tmsResponse : String? = "",
    var field39 : String,
    var authId : String?,
    var hostEmvData : HostEmvData,
    var referenceNumber : String,
    var stan : String,
    var transactionChannelName : String,
    var wasReceive : String,
    var wasSend : String,
    var terminalID : String,
    var maskedPAN : String,
    var reference : String? = "",
    var merchantName : String,
    var merchantAddress : String,
    var processingCide : String? = "",
    var cardExpiry : String,
    var amount : Int
)

data class StatusMessageObject(
    var code : String,
    var message: String
)

data class HostEmvData(
    var amountAuthorized : List<String?>,
    var amountOther : List<String?>,
    var atc : List<String?>,
    var iad : List<String?>,
    var rc : List<String?>
)
