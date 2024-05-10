package com.epawo.custodian.model.cashout

data class CardModel(
    val cardPan :String,
    val cardExpiry : String,
    val cardSequenceNumber : String,
    val cardTrackTwoNumber : String,
    val cardRRN : String,
    val cardStan : String,
    val cardIccData : String,
    val acquiringInstitutionalCode : String,
    val pinBlock : String,
    val cardAcceptorCode : String,
    val postDataCode : String,
    val customerName : String? = ""
)
