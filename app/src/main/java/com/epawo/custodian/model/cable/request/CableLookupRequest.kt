package com.epawo.custodian.model.cable.request

data class CableLookupRequest(
    var providerCode : String,
    var subscriberId : String
)
