package com.epawo.custodian.utilities

class UrlConstants {

    companion object{

        //UrlConstants
        const val BASE_URL = "https://api-test.egolepay.com/api/"
        const val LIVE_BASE_URL = "https://apitray.egolepay.com/api/"
        const val CASHOUT_BASE_URL = "https://webpos-n.azurewebsites.net/v1/"
        const val LOGIN_URL = "Account/Auth"
        const val LOAD_BANKS_LIST = "Payout/Bank"
        const val VALIDATE_ACCOUNT_NUMBER = "Payout/AccountLookup"
        const val TRANSFER_FUND_URL = "Payout/PayOut"
        const val CASHOUT_NEW_URL = "Cashout/Card-Pay" //"card-pay" //"v1/card-pay"
        const val PROCESS_TRANSACTION = "process"
        const val AUTO_REG_STATE_URL = "Vas/GetAutoRegStates"
        const val AUTO_REG_VEHICLE_DETAILS = "Vas/VehicleRegDetails"
        const val AUTO_REG_PAYMENT = "Vas/AutoRegOnlinePayment"
        const val TRANSACTION_HISTORY = "Report/TransactionsPageLookup"
        const val FORGOT_PASSWORD = "Account/ForgetPassword"
        const val RESET_PASSWORD = "Account/resetpassword"
        const val TICKET_URL = "Ticket/GetTicketByWalletID/{walletID}/{startDate}/{endDate}"

        //vas
        const val AIRTIME_URL = "VasVMP/Airtime"
        const val CATEGORY_URL = "VasVMP/ProductCategories"
        const val AIRTIME_DATA_URL = "VasVMP/DataProvider"
        const val AIRTIME_DATA_PAYMENT_URL = "VasVMP/Data"

        const val ENERGY_PROVIDER_URL = "VasVMP/EnergyServices"
        const val VALIDATE_METER_NUMBER_URL = "VasVMP/VerifyEnergyMeter"
        const val ENERGY_PAYMENT_URL = "VasVMP/Energy"

        const val CABLE_PROVIDERS_URL = "VasVMP/CableProvider"
        const val CABLE_BUNDLES_URL = "VasVMP/Cablepackages"
        const val CABLE_LOOKUP_URL = "VasVMP/CableLookup"
        const val CABLE_PAYMENT_URL = "VasVMP/CablePayment"

        //Betting
        const val BETTING_PROVIDERS = "VasVMP/BettingProvider"
        const val BETTING_LOOKUP = "VasVMP/BettingLookup"

        //Insurance
        const val INSURANCE = "Insurance/GetDetailsPolicy"
        const val INSURANCE_CASHOUT = "Insurance/CustodianPostTransaction"

        //WALLET BALANCE
        const val WALLET_BALANCE = "Account/WalletBalance/{mainAccount}"


        //AppConstants
        const val POWERED_BY = "Powered By Epawo"
        const val COMPANY_EMAIL = "hello@egolepay.com"
        const val COMPANY_PHONE = " 091**"
        const val CURRENCY_NAIRA = "₦"
        const val INTER = "inter"
        const val GENERAL_ERROR = "Your request cannot be completed. Try again!"
        const val NO_INTERNET = "No Internet, please connect to an active network"


    }
}
