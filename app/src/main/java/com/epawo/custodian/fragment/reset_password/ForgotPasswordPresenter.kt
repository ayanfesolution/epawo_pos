package com.epawo.custodian.fragment.reset_password

import com.epawo.custodian.model.forgot_password.request.ForgotPasswordRequest
import com.epawo.custodian.model.forgot_password.response.ForgotPasswordResponse

class ForgotPasswordPresenter(view : ForgotPasswordContract.ForgotPasswordView) :
    ForgotPasswordContract.ForgotPasswordListener  {


    private var view : ForgotPasswordContract.ForgotPasswordView
    private var inputAmountInteractor : ForgotPasswordInteractor

    init {
        this.view = view
        inputAmountInteractor = ForgotPasswordInteractor(this)
    }

    override fun onSuccess(response: ForgotPasswordResponse) {
        view.hideProgress()
        view.forgotPasswordSuccess(response)
    }

    override fun onFailure(response: String?) {
        view.hideProgress()
        view.showToast(response)
    }

    fun forgotPassword(token : String, request : ForgotPasswordRequest){
        view.showProgress()
        inputAmountInteractor.forgotPassword(token, request)
    }
}