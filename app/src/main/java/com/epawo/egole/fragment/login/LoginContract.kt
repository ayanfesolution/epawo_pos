package com.epawo.egole.fragment.login

import com.epawo.egole.model.login.response.LoginResponse

interface LoginContract {

    interface LoginView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onLoginSuccess(response : LoginResponse)
    }

    interface LoginListener{
        fun onLoginSuccess(response : LoginResponse)
        fun onFailure(message : String?)
    }
}