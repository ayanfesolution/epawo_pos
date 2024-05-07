package com.epawo.egole.fragment.transactions

import android.util.Log
import com.epawo.egole.fragment.login.LoginInteractor
import com.epawo.egole.interfaces.NetworkService
import com.epawo.egole.model.transaction.response.TransactionResponseModel
import com.epawo.egole.service.RetrofitInstance
import com.epawo.egole.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class TransactionInteractor(listener : TransactionContract.TransactionListener) {

    var listener : TransactionContract.TransactionListener

    init{
        this.listener = listener
    }

    fun loadTransactions(token : String, page : String){
        loadTransactionObserver(token,page).subscribeWith(loadTransactionObservable())
    }

    fun loadMore(token : String, page : String){
        loadMoreObserver(token,page).subscribeWith(loadMoreTransactionsObservable())
    }

    private fun loadMoreObserver(token : String, page : String)
            : Observable<TransactionResponseModel> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .getTransactionsHistory("Bearer $token", page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun loadTransactionObserver(token : String, page : String)
            : Observable<TransactionResponseModel> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .getTransactionsHistory("Bearer $token", page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun loadMoreTransactionsObservable(): DisposableObserver<TransactionResponseModel> {
        return object : DisposableObserver<TransactionResponseModel>() {
            override fun onNext(response: TransactionResponseModel) {
                listener.onLoadMoreSuccess(response)
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

    private fun loadTransactionObservable(): DisposableObserver<TransactionResponseModel> {
        return object : DisposableObserver<TransactionResponseModel>() {
            override fun onNext(response: TransactionResponseModel) {
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
                }
                e.printStackTrace()
            }

            override fun onComplete() {
                Log.d(LoginInteractor.LOG_IN_ENDPOINT_RESULT, "Completed")
            }
        }
    }
}