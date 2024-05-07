package com.epawo.egole.model.transaction.response

import java.io.Serializable

data class TransactionResponseModel(
    var status : Int,
    var message : String,
    var totalPage : Int,
    var currentPage : Int,
    var transactions : MutableList<Transactions>
)

data class Transactions(
    var transactionID : Int,
    var walletAccountNumber : String? = "",
    var transactionType : String? = "",
    var transactionMethod : String? = "",
    var amount : Double,
    var walletReferenceNumber : String? = "",
    var fullName : String? = "",
    var transactionDate : String? = "",
    var ipAddress : String? = "",
    var transactionReferenceNo : String? = "",
    var createdBy : Int,
    var previous : Double,
    var current : Double,
    var transactionStatus : String? = "",
    var surCharge : Double
) : Serializable
