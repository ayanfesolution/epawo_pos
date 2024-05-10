package com.epawo.custodian.fragment.airtime.airtime

import com.epawo.custodian.model.category.response.CategoryResponse

class AirtimeCategoriesPresenter(view : AirtimeCategoriesContract.AirtimeCategoriesView)
    : AirtimeCategoriesContract.AirtimeCategoriesListener {

    var view : AirtimeCategoriesContract.AirtimeCategoriesView
    var interactor : AirtimeCategoriesInteractor

    init {
        this.view = view
        interactor = AirtimeCategoriesInteractor(this)
    }

    override fun onSuccess(response: List<CategoryResponse>) {
        view.hideProgress()
        view.onSuccess(response)
    }

    override fun onFailure(message: String?) {
        view.hideProgress()
        view.showToast(message)
    }

    fun loadCategories(token : String){
        view.showProgress()
        interactor.loadCategories(token)
    }
}