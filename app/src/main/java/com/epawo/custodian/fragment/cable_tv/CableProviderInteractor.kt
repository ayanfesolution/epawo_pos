package com.epawo.custodian.fragment.cable_tv

import android.util.Log
import com.epawo.custodian.fragment.login.LoginInteractor
import com.epawo.custodian.interfaces.NetworkService
import com.epawo.custodian.model.cable.request.CableLookupRequest
import com.epawo.custodian.model.cable.request.CableProviderPackRequest
import com.epawo.custodian.model.cable.response.CableLookupResponse
import com.epawo.custodian.model.cable.response.CableProviderPackResponse
import com.epawo.custodian.model.cable.response.CableProviderResponse
import com.epawo.custodian.service.RetrofitInstance
import com.epawo.custodian.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class CableProviderInteractor(listener : CableProviderContract.CableProviderListener) {

    var listener :  CableProviderContract.CableProviderListener

    init{
        this.listener = listener
    }

    fun loadCableProvider(token : String){
        loadCableObserver(token).subscribeWith(loadCableObservable())
    }

    fun loadDetails(token : String, request : CableLookupRequest){
        loadDetailsObserver(token, request).subscribeWith(loadDetailsObservable())
    }

    fun loadBundles(token : String, request : CableProviderPackRequest){
        loadBundlesObserver(token,request).subscribeWith(loadBundlesObservable())
    }

    private fun loadDetailsObserver(token : String, request : CableLookupRequest)
            : Observable<CableLookupResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .cableLookup("Bearer $token", request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun loadBundlesObserver(token : String, request : CableProviderPackRequest)
            : Observable<CableProviderPackResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .loadCableBundles("Bearer $token", request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun loadCableObserver(token : String)
            : Observable<CableProviderResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .loadCableProviders("Bearer $token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun loadDetailsObservable(): DisposableObserver<CableLookupResponse> {
        return object : DisposableObserver<CableLookupResponse>() {
            override fun onNext(response: CableLookupResponse) {
                listener.onLookUpSuccessful(response)
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

    private fun loadBundlesObservable(): DisposableObserver<CableProviderPackResponse> {
        return object : DisposableObserver<CableProviderPackResponse>() {
            override fun onNext(response: CableProviderPackResponse) {
                listener.onLoadBundlesSuccess(response)
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

    private fun loadCableObservable(): DisposableObserver<CableProviderResponse> {
        return object : DisposableObserver<CableProviderResponse>() {
            override fun onNext(response: CableProviderResponse) {
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