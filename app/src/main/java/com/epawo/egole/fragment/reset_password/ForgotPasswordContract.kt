package com.epawo.egole.fragment.reset_password

import com.epawo.egole.model.forgot_password.response.ForgotPasswordResponse

interface ForgotPasswordContract {

    interface ForgotPasswordView{
        fun showToast(message: String?)
        fun showProgress()
        fun hideProgress()
        fun forgotPasswordSuccess(cashout: ForgotPasswordResponse)
    }

    interface ForgotPasswordListener{
        fun onSuccess(response: ForgotPasswordResponse)
        fun onFailure(response: String?)
    }
}