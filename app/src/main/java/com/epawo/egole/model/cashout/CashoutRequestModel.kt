package com.epawo.egole.model.cashout

data class CashoutRequestModel(
   var accountType : Int,
   var amount : Int,
   var CardData : CardDatas,
   var cardSequenceNumber : String,
   var iccData : String,
   var preferredChannel : Int,
   var terminalID : String
)

data class CardDatas(
    var pan : String,
    var expiryMonth : String,
    var expiryYear : String,
    var track2 : String,
    var pinBlock : String
)

