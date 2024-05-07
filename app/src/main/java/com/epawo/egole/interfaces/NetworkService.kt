package com.epawo.egole.interfaces

import com.epawo.egole.model.airtime.request.AirtimeDataRequest
import com.epawo.egole.model.airtime.request.AirtimeRechargeRequest
import com.epawo.egole.model.airtime.response.AirtimeDataPaymentResponse
import com.epawo.egole.model.airtime.response.AirtimeDataResponse
import com.epawo.egole.model.airtime.response.AirtimeRechargeResponse
import com.epawo.egole.model.auto_reg.response.AutoRegStateListResponse
import com.epawo.egole.model.betting.request.BettingLookupRequest
import com.epawo.egole.model.betting.response.BettingProviderResponse
import com.epawo.egole.model.betting.response.BettingResponseModel
import com.epawo.egole.model.cable.request.CableLookupRequest
import com.epawo.egole.model.cable.request.CablePaymentRequest
import com.epawo.egole.model.cable.request.CableProviderPackRequest
import com.epawo.egole.model.cable.response.CableLookupResponse
import com.epawo.egole.model.cable.response.CablePaymentResponse
import com.epawo.egole.model.cable.response.CableProviderPackResponse
import com.epawo.egole.model.cable.response.CableProviderResponse
import com.epawo.egole.model.cashout.CashoutRequestModel
import com.epawo.egole.model.cashout.CashoutResponseModel
import com.epawo.egole.model.category.response.CategoryResponse
import com.epawo.egole.model.energy.request.ElectricityPaymentRequest
import com.epawo.egole.model.energy.request.ValidateMeterNumberRequest
import com.epawo.egole.model.energy.response.ElectricityPaymentResponse
import com.epawo.egole.model.energy.response.EnergyProviderResponse
import com.epawo.egole.model.energy.response.ValidateMeterNumberResponse
import com.epawo.egole.model.forgot_password.request.ForgotPasswordRequest
import com.epawo.egole.model.forgot_password.response.ForgotPasswordResponse
import com.epawo.egole.model.fund_transfer.LoadBankListResponse
import com.epawo.egole.model.fund_transfer.TransferFundRequest
import com.epawo.egole.model.fund_transfer.TransferFundResponse
import com.epawo.egole.model.fund_transfer.ValidateBankRequest
import com.epawo.egole.model.fund_transfer.ValidateBankResponse
import com.epawo.egole.model.login.request.LoginRequest
import com.epawo.egole.model.login.response.LoginResponse
import com.epawo.egole.model.ticket.TicketResponseModel
import com.epawo.egole.model.transaction.response.TransactionResponseModel
import com.epawo.egole.model.wallet_balance.response.WalletBalanceResponse
import com.epawo.egole.utilities.UrlConstants
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkService {

    @POST(UrlConstants.LOGIN_URL)
    fun login(@Body request : LoginRequest) : Observable<LoginResponse>

    @GET(UrlConstants.LOAD_BANKS_LIST)
    fun loadBanks(@Header("Authorization") token : String) : Observable<LoadBankListResponse>

    @POST(UrlConstants.VALIDATE_ACCOUNT_NUMBER)
    fun validateAccount(@Header("Authorization") token : String, @Body request : ValidateBankRequest) :
            Observable<ValidateBankResponse>

    @POST(UrlConstants.TRANSFER_FUND_URL)
    fun transferFund(@Header("Authorization") token : String, @Body request : TransferFundRequest) :
            Observable<TransferFundResponse>

    @POST(UrlConstants.AIRTIME_URL)
    fun rechargeAirtime(@Header("Authorization") token : String, @Body request : AirtimeRechargeRequest)
    : Observable<AirtimeRechargeResponse>

    @GET(UrlConstants.AIRTIME_DATA_URL)
    fun rechargeAirtimeData(@Header("Authorization") token : String, @Query("Provider") provider : String)
            : Observable<AirtimeDataResponse>

    @POST(UrlConstants.AIRTIME_DATA_PAYMENT_URL)
    fun payAirtimeData(@Header("Authorization") token : String, @Body request : AirtimeDataRequest)
            : Observable<AirtimeDataPaymentResponse>

    @GET(UrlConstants.ENERGY_PROVIDER_URL)
    fun getEnergyProviders(@Header("Authorization") token : String) : Observable<EnergyProviderResponse>

    @POST(UrlConstants.VALIDATE_METER_NUMBER_URL)
    fun validateMeterNumber(@Header("Authorization") token : String, @Body request : ValidateMeterNumberRequest)
            : Observable<ValidateMeterNumberResponse>

    @POST(UrlConstants.ENERGY_PAYMENT_URL)
    fun vendEnergy(@Header("Authorization") token : String, @Body request : ElectricityPaymentRequest)
            : Observable<ElectricityPaymentResponse>

    @GET(UrlConstants.CABLE_PROVIDERS_URL)
    fun loadCableProviders(@Header("Authorization") token : String)
            : Observable<CableProviderResponse>

    @POST(UrlConstants.CABLE_BUNDLES_URL)
    fun loadCableBundles(@Header("Authorization") token : String, @Body request : CableProviderPackRequest)
            : Observable<CableProviderPackResponse>

    @POST(UrlConstants.CABLE_LOOKUP_URL)
    fun cableLookup(@Header("Authorization") token : String, @Body request : CableLookupRequest)
            : Observable<CableLookupResponse>

    @POST(UrlConstants.CABLE_PAYMENT_URL)
    fun cablePayment(@Header("Authorization") token : String, @Body request : CablePaymentRequest)
            : Observable<CablePaymentResponse>

    @GET(UrlConstants.AUTO_REG_STATE_URL)
    fun getStateList(@Header("Authorization") token : String) : Observable<List<AutoRegStateListResponse>>

    @GET(UrlConstants.CATEGORY_URL)
    fun loadCategories(@Header("Authorization") token : String) : Observable<List<CategoryResponse>>

    @GET(UrlConstants.BETTING_PROVIDERS)
    fun bettingProviders(@Header("Authorization") token : String)
            : Observable<BettingProviderResponse>

    @POST(UrlConstants.BETTING_LOOKUP)
    fun bettingLookup(@Header("Authorization") token : String, @Body request : BettingLookupRequest)
            : Observable<BettingProviderResponse>

    @POST(UrlConstants.CASHOUT_NEW_URL)
    @Headers("Accept: application/json",
        "User-Agent: PostmanRuntime/7.32.3",
        "Content-Type: application/json",
        "Accept: */*",
        "Accept-Encoding: gzip, deflate, br",
        "Connection: keep-alive")
    fun processCashout(@Header("Authorization") token : String, @Body request : CashoutRequestModel)
    : Observable<CashoutResponseModel>

    @POST(UrlConstants.TRANSACTION_HISTORY)
    fun getTransactionsHistory(@Header("Authorization") token : String, @Query("page") page : String)
            : Observable<TransactionResponseModel>

    @POST(UrlConstants.FORGOT_PASSWORD)
    fun forgotPassword(@Header("Authorization") token : String, @Body request : ForgotPasswordRequest):
            Observable<ForgotPasswordResponse>

    @GET(UrlConstants.TICKET_URL)
    fun getTicketList(@Header("Authorization") token : String, @Path("walletID") walletID : String,
                      @Path("startDate") startDate : String, @Path("endDate") endDate : String):
            Observable<TicketResponseModel>

    @GET(UrlConstants.WALLET_BALANCE)
    fun getWalletBalance(@Header("Authorization") token : String, @Path("mainAccount") mainAccount : String
    ): Observable<WalletBalanceResponse>
}