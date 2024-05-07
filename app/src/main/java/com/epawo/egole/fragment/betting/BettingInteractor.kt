package com.epawo.egole.fragment.betting

import android.util.Log
import com.epawo.egole.fragment.login.LoginInteractor
import com.epawo.egole.interfaces.NetworkService
import com.epawo.egole.model.betting.request.BettingLookupRequest
import com.epawo.egole.model.betting.response.BettingProviderResponse
import com.epawo.egole.model.category.response.CategoryResponse
import com.epawo.egole.service.RetrofitInstance
import com.epawo.egole.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class BettingInteractor(listener : BettingContract.BettingListener) {

    var listener : BettingContract.BettingListener

    init{
        this.listener = listener
    }

    fun loadBettingProviders(token : String){
        loadCategoriesObserver(token).subscribeWith(loadCategoriesObservable())
    }

    fun lookup(token : String,request : BettingLookupRequest){
        lookupObserver(token,request).subscribeWith(lookupObservable())
    }

    private fun loadCategoriesObserver(token : String): Observable<BettingProviderResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .bettingProviders("Bearer $token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun lookupObserver(token : String,request : BettingLookupRequest): Observable<BettingProviderResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .bettingProviders("Bearer $token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun lookupObservable(): DisposableObserver<BettingProviderResponse> {
        return object : DisposableObserver<BettingProviderResponse>() {
            override fun onNext(response: BettingProviderResponse) {
                listener.onLoadBettingProvider(response)
            }

            override fun onError(e: Throwable) {
                Log.d(LoginInteractor.LOG_IN_ENDPOINT_RESULT, "Error $e")
                val jsonObject: JSONObject
                try {
                    val errorMessage: String = (e as HttpException).response()?.errorBody()!!.string()
                    jsonObject = JSONObject(errorMessage)
                    listener.onFailure(jsonObject.getString("message"))
                } catch (ioException: Exception) {
                    ioException.printStackTrace()
                    listener.onFailure(UrlConstants.GENERAL_ERROR)
                }
                e.printStackTrace()
            }

            override fun onComplete() {
                Log.d(LoginInteractor.LOG_IN_ENDPOINT_RESULT, "Completed")
            }
        }
    }

    private fun loadCategoriesObservable(): DisposableObserver<BettingProviderResponse> {
        return object : DisposableObserver<BettingProviderResponse>() {
            override fun onNext(response: BettingProviderResponse) {
                listener.onLoadBettingProvider(response)
            }

            override fun onError(e: Throwable) {
                Log.d(LoginInteractor.LOG_IN_ENDPOINT_RESULT, "Error $e")
                val jsonObject: JSONObject
                try {
                    val errorMessage: String = (e as HttpException).response()?.errorBody()!!.string()
                    jsonObject = JSONObject(errorMessage)
                    listener.onFailure(jsonObject.getString("message"))
                } catch (ioException: Exception) {
                    ioException.printStackTrace()
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