package com.epawo.custodian.fragment.cable_tv

import com.epawo.custodian.model.cable.response.CableLookupResponse
import com.epawo.custodian.model.cable.response.CableProviderPackResponse
import com.epawo.custodian.model.cable.response.CableProviderResponse

interface CableProviderContract {

    interface CableProviderView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onSuccess(response : CableProviderResponse)
        fun onLoadBundlesSuccess(response : CableProviderPackResponse)
        fun onLookUpSuccessful(response : CableLookupResponse)
    }

    interface CableProviderListener{
        fun onSuccess(response : CableProviderResponse)
        fun onLookUpSuccessful(response : CableLookupResponse)
        fun onLoadBundlesSuccess(response : CableProviderPackResponse)
        fun onFailure(message : String?)
    }
}