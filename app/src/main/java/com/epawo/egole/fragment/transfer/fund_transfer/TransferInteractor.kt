package com.epawo.egole.fragment.transfer.fund_transfer

import android.util.Log
import com.epawo.egole.fragment.login.LoginInteractor
import com.epawo.egole.interfaces.NetworkService
import com.epawo.egole.model.fund_transfer.TransferFundRequest
import com.epawo.egole.model.fund_transfer.TransferFundResponse
import com.epawo.egole.service.RetrofitInstance
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class TransferInteractor(listener : TransferContract.TransferListener) {

    var fundListener : TransferContract.TransferListener

    init{
        this.fundListener = listener
    }

    fun transfer(token : String, request : TransferFundRequest){
        transferObserver(token, request).subscribeWith(transferObservable())
    }

    private fun transferObserver(token : String, request : TransferFundRequest) : Observable<TransferFundResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .transferFund("Bearer $token", request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun transferObservable(): DisposableObserver<TransferFundResponse> {
        return object : DisposableObserver<TransferFundResponse>() {
            override fun onNext(response: TransferFundResponse) {
                fundListener.onTransferSuccessful(response)
            }

            override fun onError(e: Throwable) {
                Log.d(LoginInteractor.LOG_IN_ENDPOINT_RESULT, "Error $e")
                val jsonObject: JSONObject
                try {
                    val errorMessage: String = (e as HttpException).response()?.errorBody()!!.string()
                    jsonObject = JSONObject(errorMessage)
                    fundListener.onFailure(jsonObject.getString("message"))
                } catch (ioException: Exception) {}
                fundListener.onFailure("Fund transfer failed")
                e.printStackTrace()
            }

            override fun onComplete() {
                Log.d(LoginInteractor.LOG_IN_ENDPOINT_RESULT, "Completed")
            }
        }
    }
}