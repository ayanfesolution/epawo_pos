package com.epawo.custodian.fragment.electricity

import android.util.Log
import com.epawo.custodian.fragment.login.LoginInteractor
import com.epawo.custodian.interfaces.NetworkService
import com.epawo.custodian.model.energy.request.ValidateMeterNumberRequest
import com.epawo.custodian.model.energy.response.EnergyProviderResponse
import com.epawo.custodian.model.energy.response.ValidateMeterNumberResponse
import com.epawo.custodian.service.RetrofitInstance
import com.epawo.custodian.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class ElectricityProviderInteractor(listener : ElectricityProvidersContract.ElectricityProvidersListener) {

    var listener : ElectricityProvidersContract.ElectricityProvidersListener

    init{
        this.listener = listener
    }

    fun getProviders(token : String){
        getProviderObserver(token).subscribeWith(getProviderObservable())
    }

    fun validateMeterNumber(token : String, request : ValidateMeterNumberRequest){
        validateMeterObserver(token,request).subscribeWith(validateMeterObservable())
    }

    private fun validateMeterObserver(token : String, request : ValidateMeterNumberRequest)
            : Observable<ValidateMeterNumberResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .validateMeterNumber("Bearer $token", request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getProviderObserver(token : String)
            : Observable<EnergyProviderResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .getEnergyProviders("Bearer $token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun validateMeterObservable(): DisposableObserver<ValidateMeterNumberResponse> {
        return object : DisposableObserver<ValidateMeterNumberResponse>() {
            override fun onNext(response: ValidateMeterNumberResponse) {
                listener.onValidateMeterNumberSuccess(response)
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

    private fun getProviderObservable(): DisposableObserver<EnergyProviderResponse> {
        return object : DisposableObserver<EnergyProviderResponse>() {
            override fun onNext(response: EnergyProviderResponse) {
                listener.onLoadProviderSuccess(response)
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