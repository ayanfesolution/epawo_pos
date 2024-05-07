package com.epawo.egole.model.airtime.response

data class AirtimeDataResponse(
    var message : String? = "",
    var status : String,
    var product : List<DataProducts>?,
    var phone : String,
    var network : String,
    var product_id : String? = "",
    var amount : Int,
    var productModeID : Int,
    var productID : Int,
    var type : String? = "",
    var surauth : String? = "",
    var bundle : String? = "" ,
    var request_id : Int
)

data class DataProducts(
    var price : Int,
    var code : String,
    var allowance : String,
    var validity : String
)
