package com.epawo.egole.fragment.airtime.airtime

import android.util.Log
import com.epawo.egole.fragment.login.LoginInteractor
import com.epawo.egole.interfaces.NetworkService
import com.epawo.egole.model.airtime.request.AirtimeRechargeRequest
import com.epawo.egole.model.airtime.response.AirtimeRechargeResponse
import com.epawo.egole.service.RetrofitInstance
import com.epawo.egole.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class AirtimeRechargeInteractor(listener : AirtimeRechargeContract.AirtimeRechargeListener) {

    var listener : AirtimeRechargeContract.AirtimeRechargeListener

    init{
        this.listener = listener
    }

    fun performRecharge(token : String, request : AirtimeRechargeRequest){
        rechargePhoneObserver(token,request).subscribeWith(rechargePhoneObservable())
    }

    private fun rechargePhoneObserver(token : String,  request : AirtimeRechargeRequest)
    : Observable<AirtimeRechargeResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .rechargeAirtime("Bearer $token", request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun rechargePhoneObservable(): DisposableObserver<AirtimeRechargeResponse> {
        return object : DisposableObserver<AirtimeRechargeResponse>() {
            override fun onNext(response: AirtimeRechargeResponse) {
                listener.onRechargeSuccess(response)
            }

            override fun onError(e: Throwable) {
                Log.d(LoginInteractor.LOG_IN_ENDPOINT_RESULT, "Error $e")
                val jsonObject: JSONObject
                try {
                    val errorMessage: String = (e as HttpException).response()?.errorBody()!!.string()
                    jsonObject = JSONObject(errorMessage)
                    listener.onFailure(jsonObject.getString("message"))
                } catch (ioException: Exception) {
                    listener.onFailure(UrlConstants.GENERAL_ERROR)
                }
                e.printStackTrace()
            }

            override fun onComplete() {
                Log.d(LoginInteractor.LOG_IN_ENDPOINT_RESULT, "Completed")
            }
        }
    }
}