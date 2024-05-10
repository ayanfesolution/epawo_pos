package com.epawo.custodian.fragment.airtime.airtime

import com.epawo.custodian.model.category.response.CategoryResponse

interface AirtimeCategoriesContract {

    interface AirtimeCategoriesView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onSuccess(response : List<CategoryResponse>)
    }

    interface AirtimeCategoriesListener{
        fun onSuccess(response : List<CategoryResponse>)
        fun onFailure(message : String?)
    }
}