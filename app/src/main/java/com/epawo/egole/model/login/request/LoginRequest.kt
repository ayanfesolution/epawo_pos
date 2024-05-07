package com.epawo.egole.model.login.request

data class LoginRequest(
    var EmailOrPhone : String,
    var password : String,
    var deviceType : String,
    var deviceName : String,
    var userAgent : String,
    var brandName : String,
    var brand : String,
    var deviceSerialNumber : String
)
