package com.epawo.egole.fragment.home

import android.util.Log
import com.epawo.egole.fragment.login.LoginInteractor
import com.epawo.egole.interfaces.NetworkService
import com.epawo.egole.model.wallet_balance.response.WalletBalanceResponse
import com.epawo.egole.service.RetrofitInstance
import com.epawo.egole.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class HomeInteractor(listener : HomeContract.HomeListener) {

    var listener : HomeContract.HomeListener

    init{
        this.listener = listener
    }

    fun loadBalance(token : String, accountNumber : String){
        loadBalanceObserver(token,accountNumber).subscribeWith(loadBalanceObservable())
    }

    private fun loadBalanceObserver(token : String, accountNumber : String): Observable<WalletBalanceResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .getWalletBalance("Bearer $token", accountNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun loadBalanceObservable(): DisposableObserver<WalletBalanceResponse> {
        return object : DisposableObserver<WalletBalanceResponse>() {
            override fun onNext(response: WalletBalanceResponse) {
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