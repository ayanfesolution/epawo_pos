package com.epawo.custodian.fragment.cable_tv

import com.epawo.custodian.model.cable.request.CableLookupRequest
import com.epawo.custodian.model.cable.request.CableProviderPackRequest
import com.epawo.custodian.model.cable.response.CableLookupResponse
import com.epawo.custodian.model.cable.response.CableProviderPackResponse
import com.epawo.custodian.model.cable.response.CableProviderResponse

class CableProviderPresenter(view : CableProviderContract.CableProviderView)
    : CableProviderContract.CableProviderListener {

    var view : CableProviderContract.CableProviderView
    var interactor : CableProviderInteractor

    init {
        this.view = view
        interactor = CableProviderInteractor(this)
    }

    override fun onSuccess(response: CableProviderResponse) {
        view.hideProgress()
        view.onSuccess(response)
    }

    override fun onLookUpSuccessful(response: CableLookupResponse) {
        view.hideProgress()
        view.onLookUpSuccessful(response)
    }

    override fun onLoadBundlesSuccess(response: CableProviderPackResponse) {
        view.hideProgress()
        view.onLoadBundlesSuccess(response)
    }

    override fun onFailure(message: String?) {
        view.hideProgress()
        view.showToast(message)
    }

    fun loadCableProvider(token : String){
        view.showProgress()
        interactor.loadCableProvider(token)
    }

    fun loadProviderBundles(token : String, request : CableProviderPackRequest){
        view.showProgress()
        interactor.loadBundles(token, request)
    }

    fun loadCustomerDetails(token : String, request : CableLookupRequest){
        view.showProgress()
        interactor.loadDetails(token, request)
    }
}