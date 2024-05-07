package com.epawo.egole.fragment.login


import android.util.Log
import com.epawo.egole.interfaces.NetworkService
import com.epawo.egole.model.login.request.LoginRequest
import com.epawo.egole.model.login.response.LoginResponse
import com.epawo.egole.service.RetrofitInstance
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class LoginInteractor(listener : LoginContract.LoginListener) {

    var loginListener : LoginContract.LoginListener

    init{
        this.loginListener = listener
    }

    fun performLogin(request : LoginRequest){
        loginUserObserver(request).subscribeWith(getLoginUserObservable())
   }

    private fun loginUserObserver(loginRequestModel: LoginRequest) : Observable<LoginResponse> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .login(loginRequestModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getLoginUserObservable(): DisposableObserver<LoginResponse> {
        return object : DisposableObserver<LoginResponse>() {
            override fun onNext(loginResponseModel: LoginResponse) {
                loginListener.onLoginSuccess(loginResponseModel)
            }

            override fun onError(e: Throwable) {
                Log.d(LOG_IN_ENDPOINT_RESULT, "Error $e")
                val jsonObject: JSONObject
                try {
                    val errorMessage: String = (e as HttpException).response()?.errorBody()!!.string()
                    jsonObject = JSONObject(errorMessage)
                    loginListener.onFailure(jsonObject.getJSONArray("error").getString(0))
                } catch (ioException: Exception) {
                    loginListener.onFailure("Login failed, try again later....")
                }
                e.printStackTrace()
            }

            override fun onComplete() {
                Log.d(LOG_IN_ENDPOINT_RESULT, "Completed")
            }
        }
    }

    companion object{
        const val LOG_IN_ENDPOINT_RESULT = "login_endpoint"
    }
}