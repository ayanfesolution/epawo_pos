package com.epawo.egole.fragment.airtime.airtime

import android.util.Log
import com.epawo.egole.fragment.login.LoginInteractor
import com.epawo.egole.interfaces.NetworkService
import com.epawo.egole.model.category.response.CategoryResponse
import com.epawo.egole.service.RetrofitInstance
import com.epawo.egole.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class AirtimeCategoriesInteractor(listener : AirtimeCategoriesContract.AirtimeCategoriesListener) {

    var listener : AirtimeCategoriesContract.AirtimeCategoriesListener

    init{
        this.listener = listener
    }

    fun loadCategories(token : String){
        loadCategoriesObserver(token).subscribeWith(loadCategoriesObservable())
    }

    private fun loadCategoriesObserver(token : String): Observable<List<CategoryResponse>> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .loadCategories("Bearer $token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun loadCategoriesObservable(): DisposableObserver<List<CategoryResponse>> {
        return object : DisposableObserver<List<CategoryResponse>>() {
            override fun onNext(response: List<CategoryResponse>) {
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