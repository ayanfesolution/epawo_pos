package com.epawo.custodian.fragment.electricity

import android.util.Log
import com.epawo.custodian.fragment.login.LoginInteractor
import com.epawo.custodian.interfaces.NetworkService
import com.epawo.custodian.model.energy.request.ElectricityPaymentRequest
import com.epawo.custodian.model.energy.response.ElectricityPaymentResponse
import com.epawo.custodian.service.RetrofitInstance
import com.epawo.custodian.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class ElectricityPaymentInteractor(listener : ElectricityPaymentContract.ElectricityPaymentListener) {

    var listener : ElectricityPaymentContract.ElectricityPaymentListener

    init{
        this.listener = listener
    }

    fun makePayment(token : String, request : ElectricityPaymentRequest){
        makePaymentObserver(token, request).subscribeWith(makePaymentObservable())
    }

    private fun makePaymentObserver(token : String, request : ElectricityPaymentRequest)
            : Observable<ElectricityPaymentResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .vendEnergy("Bearer $token", request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun makePaymentObservable(): DisposableObserver<ElectricityPaymentResponse> {
        return object : DisposableObserver<ElectricityPaymentResponse>() {
            override fun onNext(response: ElectricityPaymentResponse) {
                listener.onPaymentSuccessful(response)
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