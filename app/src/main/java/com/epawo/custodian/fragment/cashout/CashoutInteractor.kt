package com.epawo.custodian.fragment.cashout

import android.util.Log
import com.epawo.custodian.interfaces.NetworkService
import com.epawo.custodian.model.cashout.*
import com.epawo.custodian.service.RetrofitInstance
import com.epawo.custodian.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class CashoutInteractor(listener : CashoutContract.CashoutListener) {

    private var listener: CashoutContract.CashoutListener

    init {
        this.listener = listener
    }

    fun doCashOut(token: String, request : CashoutRequestModel){
        cashOutObserver(token,request).subscribeWith(cashOutObservable())
    }

    private fun cashOutObserver(token: String, request: CashoutRequestModel)
            : Observable<CashoutResponseModel> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .processCashout("Bearer $token",request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun cashOutObservable(): DisposableObserver<CashoutResponseModel> {
        return object : DisposableObserver<CashoutResponseModel>() {
            override fun onNext(cashoutResponseModel: CashoutResponseModel) {
                if (cashoutResponseModel.content == null) {
                    listener.onException(cashoutResponseModel.result.message)
                } else {
                    if(cashoutResponseModel.content.statusCode == "00") {
                        listener.onSuccess(cashoutResponseModel)
                    }else{
                        listener.onException(cashoutResponseModel.content.description)
                    }
                }
            }

            override fun onError(e: Throwable) {
                Log.d(CASHOUT, "Error $e")
                val jsonObject: JSONObject
                try {
                    val errorMessage = (e as HttpException).response()!!.errorBody()!!.string()
                    jsonObject = JSONObject(errorMessage)
                    listener.onFailure(jsonObject.getJSONObject("error").getString("message").toString())
                }catch (ioException: Exception) {
                    listener.onException(UrlConstants.GENERAL_ERROR)
                }
            }
            override fun onComplete() {
                Log.d(CASHOUT, "Completed")
            }
        }
    }

    companion object{
        const val CASHOUT = "cashout"
    }
}