package com.epawo.custodian.fragment.auto_reg

import android.util.Log
import com.epawo.custodian.fragment.login.LoginInteractor
import com.epawo.custodian.interfaces.NetworkService
import com.epawo.custodian.model.auto_reg.response.AutoRegStateListResponse
import com.epawo.custodian.service.RetrofitInstance
import com.epawo.custodian.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class AutoRegStateInteractor(listener : AutoRegStateContract.AutoRegStateListener) {

    var listener : AutoRegStateContract.AutoRegStateListener

    init{
        this.listener = listener
    }

    fun load(token : String){
        loadStateObserver(token).subscribeWith(loadStateObservable())
    }

    private fun loadStateObserver(token : String)
            : Observable<List<AutoRegStateListResponse>> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .getStateList("Bearer $token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    private fun loadStateObservable(): DisposableObserver<List<AutoRegStateListResponse>> {
        return object : DisposableObserver<List<AutoRegStateListResponse>>() {
            override fun onNext(response: List<AutoRegStateListResponse>) {
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