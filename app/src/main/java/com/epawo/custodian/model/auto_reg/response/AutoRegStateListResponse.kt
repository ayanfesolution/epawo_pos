package com.epawo.custodian.model.auto_reg.response

data class AutoRegStateListResponse(
    var message : String? = "",
    var stateName : String,
    var stateCode : String? = "",
    var stateId : Int

)
