package com.epawo.egole.fragment.airtime.data

import com.epawo.egole.model.airtime.response.AirtimeDataResponse


class DataRechargePresenter(view : DataRechargeContract.DataRechargeView)
    : DataRechargeContract.DataRechargeListener {

    var view : DataRechargeContract.DataRechargeView
    var interactor : DataRechargeInteractor

    init {
        this.view = view
        interactor = DataRechargeInteractor(this)
    }

    override fun onLoadBundles(response: AirtimeDataResponse) {
        view.hideProgress()
        view.onLoadBundles(response)
    }

    override fun onFailure(message: String?) {
       view.hideProgress()
       view.showToast(message)
    }

    fun loadDataBundles(token : String, provider : String){
        view.showProgress()
        interactor.recharge(token, provider)
    }
}