package com.epawo.egole.model.login.response

data class LoginResponse(
    val profileViews : ProfileView,
    val token : String,
    val statusCode : String,
    val result : Boolean,
    var error : List<String>,
    val deviceInfo : List<DeviceInfo>,
    val message : String,
    val usertype : String? = "",
    var wallets : String? = "",
    var userid : String,
    var expiryDate : String
)

data class ProfileView(
    val userID : Int,
    val firstName : String,
    val middleName : String,
    val lastName : String,
    val dateCreated : String,
    val businessName : String? = "",
    val businessDescription : String? = "",
    val businessAddress : String? = "",
    val companyName : String,
    val dateOfBirth : String,
    val image : String? = "",
    val walletId : Int,
    val email : String,
    val phoneNumber : String,
    val address : String? = "",
    val gender : String? = "",
    val isActive : Boolean,
    val lastLoginDate : String,
    val rcNumber : String? = "",
    val role : String,
    val roleID : String,
    val residentialState : String,
    val lga : String? = "",
    val mainWallet : String,
    val accountType : String? = "",
    val accountTypeID : String? = "",
    val terminalId : String? = "",
    val wallets : List<Wallets>,
    val token : String? = "",
    val statusCode : String,
    val result : String,
    val message : String,
    val error : String? = "",
    val walletBalance : Double,
    val openingBalance : Double,
    val closingBalance : Double,
    val sysUserType : String? = "",
    val aDeviceInfo : ADeviceInfo,
    val isSetPin : Boolean,
    val usertype : String
)

data class ADeviceInfo(
    val userDeviceId : Int,
    val deviceName : String,
    val deviceType : String,
    val ipAddress : String? = "",
    val userAgent : String,
    val dateRegistered : String,
    val userId : Int,
    val isActive : Boolean,
    val isEmailSent : Boolean,
    val requestID : String? = "",
    val dateEmailSent : String,
    val isApproved : Boolean,
    val approvedBy : Int,
    val terminalID : String? = "",
    val dateApproved : String,

)

data class DeviceInfo(
    val deviceName : String,
    val deviceType : String,
    val userAgent : String,
    val brandName : String,
    val brand : String,
    val message : String,
    val statusCode : String,
    val result : Boolean
)

data class Wallets(
    val accountNumber : String,
    val bankName : String,
    val walletName : String,
    val isActive : Boolean
)

