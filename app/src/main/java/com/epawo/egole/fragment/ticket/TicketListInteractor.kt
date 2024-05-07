package com.epawo.egole.fragment.ticket

import android.util.Log
import com.epawo.egole.fragment.login.LoginInteractor
import com.epawo.egole.interfaces.NetworkService
import com.epawo.egole.model.ticket.TicketResponseModel
import com.epawo.egole.service.RetrofitInstance
import com.epawo.egole.utilities.UrlConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class TicketListInteractor(listener : TicketListContract.TicketListListener) {

    private var listener: TicketListContract.TicketListListener

    init {
        this.listener = listener
    }

    fun loadTickets(token : String, walletId : String, startDate : String, endDate : String) {
        ticketListObserver(token,walletId,startDate,endDate).subscribeWith(ticketListObservable())
    }

    private fun ticketListObserver(token : String, walletId : String, startDate : String, endDate : String)
            : Observable<TicketResponseModel> {
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .getTicketList("Bearer $token",walletId,startDate,endDate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun ticketListObservable(): DisposableObserver<TicketResponseModel> {
        return object : DisposableObserver<TicketResponseModel>() {
            override fun onNext(response: TicketResponseModel) {
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