package com.epawo.egole.model.insurance

data class InsuranceCashoutResponse(
    val `data`: CashoutData,
    val hash: CashoutHash,
    val message: String,
    val status: Int,
    val vehiclelist: Any
)

data class CashoutData(
    val RecieptUrl: String,
    val message: String
)

data class CashoutHash(
    val checksum: String,
    val message: String
)