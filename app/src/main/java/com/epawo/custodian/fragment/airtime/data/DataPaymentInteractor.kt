package com.epawo.custodian.fragment.airtime.data

import android.util.Log
import com.epawo.custodian.fragment.login.LoginInteractor
import com.epawo.custodian.interfaces.NetworkService
import com.epawo.custodian.model.airtime.request.AirtimeDataRequest
import com.epawo.custodian.model.airtime.response.AirtimeDataPaymentResponse
import com.epawo.custodian.service.RetrofitInstance
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class DataPaymentInteractor(listener : DataPaymentContract.DataPaymentListener) {

    var listener : DataPaymentContract.DataPaymentListener

    init{
        this.listener = listener
    }

    fun makePayment(token : String, request : AirtimeDataRequest){
        makePaymentObserver(token,request).subscribeWith(makePaymentObservable())
    }

    private fun makePaymentObserver(token : String,  request : AirtimeDataRequest)
            : Observable<AirtimeDataPaymentResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .payAirtimeData("Bearer $token", request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun makePaymentObservable(): DisposableObserver<AirtimeDataPaymentResponse> {
        return object : DisposableObserver<AirtimeDataPaymentResponse>() {
            override fun onNext(response: AirtimeDataPaymentResponse) {
                listener.onPaymentSuccessful(response)
            }

            override fun onError(e: Throwable) {
                Log.d(LoginInteractor.LOG_IN_ENDPOINT_RESULT, "Error $e")
                val jsonObject: JSONObject
                try {
                    val errorMessage: String = (e as HttpException).response()?.errorBody()!!.string()
                    jsonObject = JSONObject(errorMessage)
                    listener.onFailure(jsonObject.getString("message"))
                } catch (ioException: Exception) {}
                e.printStackTrace()
            }

            override fun onComplete() {
                Log.d(LoginInteractor.LOG_IN_ENDPOINT_RESULT, "Completed")
            }
        }
    }
}