package com.epawo.egole.fragment.cable_tv

import com.epawo.egole.model.cable.response.CableLookupResponse
import com.epawo.egole.model.cable.response.CableProviderPackResponse
import com.epawo.egole.model.cable.response.CableProviderResponse

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