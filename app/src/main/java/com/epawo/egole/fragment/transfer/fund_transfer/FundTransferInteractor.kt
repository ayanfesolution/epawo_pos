package com.epawo.egole.fragment.transfer.fund_transfer

import android.util.Log
import com.epawo.egole.fragment.login.LoginInteractor
import com.epawo.egole.interfaces.NetworkService
import com.epawo.egole.model.fund_transfer.LoadBankListResponse
import com.epawo.egole.model.fund_transfer.ValidateBankRequest
import com.epawo.egole.model.fund_transfer.ValidateBankResponse
import com.epawo.egole.service.RetrofitInstance
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class FundTransferInteractor(listener : FundTransferContract.FundTransferListener) {

    var fundListener : FundTransferContract.FundTransferListener

    init{
        this.fundListener = listener
    }

    fun loadBank(token : String){
        loadBankObserver(token).subscribeWith(getLoadBankObservable())
    }

    fun validateAccountNumber(token : String,request : ValidateBankRequest){
        validateAccountObserver(token, request).subscribeWith(getValidateAccountObservable())
    }

    private fun validateAccountObserver(token : String, request : ValidateBankRequest) : Observable<ValidateBankResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .validateAccount("Bearer $token", request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun loadBankObserver(token: String) : Observable<LoadBankListResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .loadBanks("Bearer $token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getLoadBankObservable(): DisposableObserver<LoadBankListResponse> {
        return object : DisposableObserver<LoadBankListResponse>() {
            override fun onNext(response: LoadBankListResponse) {
                fundListener.onBankLoadSuccessful(response)
            }

            override fun onError(e: Throwable) {
                Log.d(LoginInteractor.LOG_IN_ENDPOINT_RESULT, "Error $e")
                val jsonObject: JSONObject
                try {
                    val errorMessage: String = (e as HttpException).response()?.errorBody()!!.string()
                    jsonObject = JSONObject(errorMessage)
                    fundListener.onFailure(jsonObject.getString("message"))
                } catch (ioException: Exception) {
                    fundListener.onFailure("Error loading bank lists")
                }
                e.printStackTrace()
            }

            override fun onComplete() {
                Log.d(LoginInteractor.LOG_IN_ENDPOINT_RESULT, "Completed")
            }
        }
    }


    private fun getValidateAccountObservable(): DisposableObserver<ValidateBankResponse> {
        return object : DisposableObserver<ValidateBankResponse>() {
            override fun onNext(response: ValidateBankResponse) {
                fundListener.onValidateAccountNumberSuccess(response)
            }

            override fun onError(e: Throwable) {
                Log.d(LoginInteractor.LOG_IN_ENDPOINT_RESULT, "Error $e")
                val jsonObject: JSONObject
                try {
                    val errorMessage: String = (e as HttpException).response()?.errorBody()!!.string()
                    jsonObject = JSONObject(errorMessage)
                    fundListener.onFailure(jsonObject.getString("message"))
                } catch (ioException: Exception) {
                    fundListener.onFailure("Error validating account number")
                }
                e.printStackTrace()
            }

            override fun onComplete() {
                Log.d(LoginInteractor.LOG_IN_ENDPOINT_RESULT, "Completed")
            }
        }
    }
}