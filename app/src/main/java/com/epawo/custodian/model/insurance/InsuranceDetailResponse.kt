package com.epawo.custodian.model.insurance

data class InsuranceDetailResponse(
    val `data`: Data,
    val hash: Hash,
    val message: String,
    val status: Int,
    val vehiclelist: Any
)

data class Data(
    val agenctNameField: String,
    val agenctNumField: String,
    val bizBranchField: String,
    val bizUnitField: String,
    val dOBField: String,
    val enddateField: String,
    val insAddr1Field: String,
    val insAddr2Field: String,
    val insAddr3Field: String,
    val insLGAField: String,
    val insOccupField: String,
    val insStateField: String,
    val instPremiumField: Int,
    val insuredEmailField: String,
    val insuredNameField: String,
    val insuredNumField: String,
    val insuredOthNameField: String,
    val insuredTelNumField: String,
    val mPremiumField: Int,
    val outPremiumField: Int,
    val policyEBusinessField: String,
    val policyNoField: String,
    val propertyChanged: PropertyChanged,
    val startdateField: String,
    val sumInsField: Int,
    val telNumField: String
)

data class Hash(
    val checksum: String,
    val message: String
)

class PropertyChanged