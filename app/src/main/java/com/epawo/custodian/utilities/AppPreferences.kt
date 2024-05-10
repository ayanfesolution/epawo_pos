package com.epawo.custodian.utilities

import android.content.Context

class AppPreferences {

    companion object{

        val PREF_NAME = "EGOLE"
        val USER_TOKEN = "user_token"
        val USER_ID = "user_id"
        val USER_WALLET_ID = "wallet_id"
        val WALLET_ACCOUNT_NUMBER = "wallet_account_number"
        val COMPANY_NAME = "company_name"
        val ADDRESS = "address"
        val EMAIL = "email"
        val PHONE = "phone"
        val TERMINAL_ID = "terminal_id"
        val BUSINESS_NAME = "business_name"
        val BUSINESS_ADDRESS = "business_address"
        val BUSINESS_MAIL = "business_mail"
        val BUSINESS_PHONE = "business_phone"
        val WALLET_BALANCE = "wallet_balance"
        val LOGIN_DATE = "login_date"
        val BANK_NAME = "bank_name"
        val WALLET_NAME = "wallet_name"
        val MAIN_WALLET = "main_wallet"
        val WALLET_LIST = "wallet_list"
    }

    fun setWalletList(context: Context, walletList: String?) {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(WALLET_LIST, walletList)
        editor.apply()
        editor.commit()
    }

    fun getWalletList(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(
            WALLET_LIST,
            ""
        )
    }

    fun setMainWalletNumber(context: Context, mainWallet: String?) {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(MAIN_WALLET, mainWallet)
        editor.apply()
        editor.commit()
    }

    fun getMainWalletNumber(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(
            MAIN_WALLET,
            ""
        )
    }

    fun setBankName(context: Context, bankName: String?) {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(BANK_NAME, bankName)
        editor.apply()
        editor.commit()
    }

    fun getBankName(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(
            BANK_NAME,
            ""
        )
    }

    fun setWalletName(context: Context, walletName: String?) {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(WALLET_NAME, walletName)
        editor.apply()
        editor.commit()
    }

    fun getWalletName(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(
            WALLET_NAME,
            ""
        )
    }

    fun setLoginDate(context: Context, agentName: String?) {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(LOGIN_DATE, agentName)
        editor.apply()
        editor.commit()
    }

    fun getLoginDate(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(
            LOGIN_DATE,
            ""
        )
    }

    fun setWalletBalance(context: Context, walletBalance : String){
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(WALLET_BALANCE, walletBalance)
        editor.apply()
        editor.commit()
    }

    fun getWalletBalance(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(WALLET_BALANCE, "")
    }

    fun setBusinessPhone(context: Context, businessPhone : String){
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(BUSINESS_PHONE, businessPhone)
        editor.apply()
        editor.commit()
    }

    fun getBusinessPhone(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(BUSINESS_PHONE, "")
    }

    fun setBusinessMail(context: Context, businessMail : String){
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(BUSINESS_MAIL, businessMail)
        editor.apply()
        editor.commit()
    }

    fun getBusinessMail(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(BUSINESS_MAIL, "")
    }

    fun setBusinessAddress(context: Context, businessAddress : String){
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(BUSINESS_ADDRESS, businessAddress)
        editor.apply()
        editor.commit()
    }

    fun getBusinessAddress(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(BUSINESS_ADDRESS, "")
    }

    fun setBusinessName(context: Context, businessName : String){
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(BUSINESS_NAME, businessName)
        editor.apply()
        editor.commit()
    }

    fun getBusinessName(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(BUSINESS_NAME, "")
    }


    fun setTerminalId(context: Context, terminalId : String){
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(TERMINAL_ID, terminalId)
        editor.apply()
        editor.commit()
    }

    fun getTerminalId(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(TERMINAL_ID, "")
    }


    fun setPhone(context: Context, phone : String){
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(PHONE, phone)
        editor.apply()
        editor.commit()
    }

    fun getPhone(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(PHONE, "")
    }

    fun setEmail(context: Context, email : String){
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(EMAIL, email)
        editor.apply()
        editor.commit()
    }

    fun getEmail(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(EMAIL, "")
    }
    fun setAddress(context: Context, address : String){
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(ADDRESS, address)
        editor.apply()
        editor.commit()
    }

    fun getAddress(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(ADDRESS, "")
    }

    fun setCompanyName(context: Context, companyName : String){
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(COMPANY_NAME, companyName)
        editor.apply()
        editor.commit()
    }

    fun getCompanyName(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(COMPANY_NAME, "")
    }
    fun setWalletAccountNumber(context: Context, accountNumber : String){
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(WALLET_ACCOUNT_NUMBER, accountNumber)
        editor.apply()
        editor.commit()
    }

    fun getWalletAccountNumber(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(WALLET_ACCOUNT_NUMBER, "")
    }
    fun setWalletId(context: Context, walletId : String){
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(USER_WALLET_ID, walletId)
        editor.apply()
        editor.commit()
    }

    fun getWalletId(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(USER_WALLET_ID, "")
    }
    fun setUserToken(context: Context, token : String){
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
        editor.commit()
    }

    fun getUserToken(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(USER_TOKEN, "")
    }

    fun setUserId(context: Context, userId : String){
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPreferences.edit()
        editor.putString(USER_ID, userId)
        editor.apply()
        editor.commit()
    }

    fun getUserId(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(PREF_NAME, 0)
        return sharedPreferences.getString(USER_ID, "")
    }
}