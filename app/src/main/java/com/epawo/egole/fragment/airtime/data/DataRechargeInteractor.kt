package com.epawo.egole.fragment.airtime.data

import android.util.Log
import com.epawo.egole.fragment.login.LoginInteractor
import com.epawo.egole.interfaces.NetworkService
import com.epawo.egole.model.airtime.response.AirtimeDataResponse
import com.epawo.egole.service.RetrofitInstance
import com.epawo.egole.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class DataRechargeInteractor(listener : DataRechargeContract.DataRechargeListener) {

    var listener : DataRechargeContract.DataRechargeListener

    init{
        this.listener = listener
    }

    fun recharge(token : String, provider : String){
        rechargeDataObserver(token,provider).subscribeWith(rechargeDataObservable())
    }

    private fun rechargeDataObserver(token : String,  provider : String)
            : Observable<AirtimeDataResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .rechargeAirtimeData("Bearer $token", provider)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun rechargeDataObservable(): DisposableObserver<AirtimeDataResponse> {
        return object : DisposableObserver<AirtimeDataResponse>() {
            override fun onNext(response: AirtimeDataResponse) {
                listener.onLoadBundles(response)
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