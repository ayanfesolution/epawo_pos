package com.epawo.egole.fragment.insurance

import android.util.Log
import com.epawo.egole.fragment.login.LoginInteractor
import com.epawo.egole.interfaces.NetworkService
import com.epawo.egole.model.insurance.InsuranceCashoutRequest
import com.epawo.egole.model.insurance.InsuranceCashoutResponse
import com.epawo.egole.model.insurance.InsuranceDetailResponse
import com.epawo.egole.service.RetrofitInstance
import com.epawo.egole.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class InsuranceCashoutInteractor(listener: InsurancePolicyContract.InsuranceCashoutListener) {

    var listener : InsurancePolicyContract.InsuranceCashoutListener

    init {
        this.listener  = listener
    }

    fun insuranceCashout(token: String, request: InsuranceCashoutRequest){
        loadInsuranceObserver(token, request).subscribeWith(insuranceCashoutObservable())
    }


    private fun loadInsuranceObserver(token: String, request: InsuranceCashoutRequest): Observable<InsuranceCashoutResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .insuranceCashout("Bearer $token", request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun insuranceCashoutObservable(): DisposableObserver<InsuranceCashoutResponse> {
        return object : DisposableObserver<InsuranceCashoutResponse>() {
            override fun onNext(response: InsuranceCashoutResponse) {
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