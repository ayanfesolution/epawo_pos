package com.epawo.egole.fragment.cable_tv

import android.util.Log
import com.epawo.egole.fragment.login.LoginInteractor
import com.epawo.egole.interfaces.NetworkService
import com.epawo.egole.model.cable.request.CablePaymentRequest
import com.epawo.egole.model.cable.response.CablePaymentResponse
import com.epawo.egole.service.RetrofitInstance
import com.epawo.egole.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class CablePaymentInteractor(listener : CablePaymentContract.CablePaymentListener) {

    var listener : CablePaymentContract.CablePaymentListener

    init{
        this.listener = listener
    }

    fun cablePayment(token : String, request : CablePaymentRequest){
        cablePaymentObserver(token,request).subscribeWith(cablePaymentObservable())
    }

    private fun cablePaymentObserver(token : String,request : CablePaymentRequest)
            : Observable<CablePaymentResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .cablePayment("Bearer $token", request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    private fun cablePaymentObservable(): DisposableObserver<CablePaymentResponse> {
        return object : DisposableObserver<CablePaymentResponse>() {
            override fun onNext(response: CablePaymentResponse) {
                listener.onSuccess(response)
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
                    ioException.printStackTrace()
                }
                e.printStackTrace()
            }

            override fun onComplete() {
                Log.d(LoginInteractor.LOG_IN_ENDPOINT_RESULT, "Completed")
            }
        }
    }
}