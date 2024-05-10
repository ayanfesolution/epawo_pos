package com.epawo.custodian.model.cashout

data class CashoutErrorResponse(
    var success : Boolean,
    var data : Datas,
    var message : String
)

data class Datas(
    var _id : String,
    var net_amount : Int,
    var fee : Int,
    var amount : Int,
    var reference : String,
    var terminal_id : String,
    var pan : String,
    var stan : String,
    var rrn : String,
    var status : String,
    var status_code : String,
    var type : String,
    var description : String,
    var card_type : String,
    var customer_reference : String?,
    var customer_info : String?,
    var created_at : String,
    var updated_at : String
)
