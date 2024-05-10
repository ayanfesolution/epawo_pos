package com.epawo.custodian.fragment.auto_reg

import com.epawo.custodian.model.auto_reg.response.AutoRegStateListResponse

class AutoRegStatePresenter(view : AutoRegStateContract.AutoRegStateView)
    : AutoRegStateContract.AutoRegStateListener {


    var view : AutoRegStateContract.AutoRegStateView
    var interactor : AutoRegStateInteractor

    init {
        this.view = view
        interactor = AutoRegStateInteractor(this)
    }
    override fun onSuccess(response: List<AutoRegStateListResponse>) {
        view.hideProgress()
        view.onSuccess(response)
    }

    override fun onFailure(message: String?) {
        view.hideProgress()
        view.showToast(message)
    }

    fun loadStates(token : String){
        view.showProgress()
        interactor.load(token)
    }

    fun getVehicleDetails(token : String){

    }
}