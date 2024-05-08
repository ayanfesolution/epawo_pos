package com.epawo.egole.fragment.insurance

import android.util.Log
import com.epawo.egole.fragment.login.LoginInteractor
import com.epawo.egole.interfaces.NetworkService
import com.epawo.egole.model.cable.response.CableLookupResponse
import com.epawo.egole.model.insurance.InsuranceDetailResponse
import com.epawo.egole.model.insurance.InsuranceDetailsRequest
import com.epawo.egole.service.RetrofitInstance
import com.epawo.egole.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class InsuranceInteractor(listener: InsurancePolicyContract.InsurancePolicyListener) {
    var listener : InsurancePolicyContract.InsurancePolicyListener

    init{
        this.listener = listener
    }

    fun getInsurancePolicyDetails(token: String, request: InsuranceDetailsRequest){
        loadInsuranceObserver(token, request).subscribeWith(insuranceDetailsObservable())
    }


    private fun loadInsuranceObserver(token: String, request: InsuranceDetailsRequest): Observable<InsuranceDetailResponse>{
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .insuranceDetails("Bearer $token", request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun insuranceDetailsObservable(): DisposableObserver<InsuranceDetailResponse> {
        return object : DisposableObserver<InsuranceDetailResponse>() {
            override fun onNext(response: InsuranceDetailResponse) {
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