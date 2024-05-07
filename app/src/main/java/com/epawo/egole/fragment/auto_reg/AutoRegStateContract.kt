package com.epawo.egole.fragment.auto_reg

import com.epawo.egole.model.auto_reg.response.AutoRegStateListResponse

interface AutoRegStateContract {

    interface AutoRegStateView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onSuccess(response : List<AutoRegStateListResponse>)
    }

    interface AutoRegStateListener{
        fun onSuccess(response : List<AutoRegStateListResponse>)
        fun onFailure(message : String?)
    }
}