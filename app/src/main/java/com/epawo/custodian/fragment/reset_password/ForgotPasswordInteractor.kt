package com.epawo.custodian.fragment.reset_password

import android.util.Log
import com.epawo.custodian.fragment.login.LoginInteractor
import com.epawo.custodian.interfaces.NetworkService
import com.epawo.custodian.model.forgot_password.request.ForgotPasswordRequest
import com.epawo.custodian.model.forgot_password.response.ForgotPasswordResponse
import com.epawo.custodian.service.RetrofitInstance
import com.epawo.custodian.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class ForgotPasswordInteractor(listener : ForgotPasswordContract.ForgotPasswordListener) {

    private var listener: ForgotPasswordContract.ForgotPasswordListener

    init {
        this.listener = listener
    }

    fun forgotPassword(token: String, request: ForgotPasswordRequest) {
        forgotPasswordObserver(token,request).subscribeWith(forgotPasswordObservable())
    }

    private fun forgotPasswordObserver(token: String, request: ForgotPasswordRequest)
            : Observable<ForgotPasswordResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .forgotPassword("Bearer $token",request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun forgotPasswordObservable(): DisposableObserver<ForgotPasswordResponse> {
        return object : DisposableObserver<ForgotPasswordResponse>() {
            override fun onNext(response: ForgotPasswordResponse) {
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