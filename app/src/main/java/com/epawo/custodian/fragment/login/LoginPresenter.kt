package com.epawo.custodian.fragment.login

import com.epawo.custodian.model.login.request.LoginRequest
import com.epawo.custodian.model.login.response.LoginResponse

class LoginPresenter(loginView : LoginContract.LoginView) : LoginContract.LoginListener {

    var loginView : LoginContract.LoginView
    var loginInteractor : LoginInteractor

    init {
        this.loginView = loginView
        loginInteractor = LoginInteractor(this)
    }

    override fun onLoginSuccess(response: LoginResponse) {
        loginView.hideProgress()
        loginView.onLoginSuccess(response)
    }

    override fun onFailure(message: String?) {
        loginView.hideProgress()
        loginView.showToast(message)
    }

    fun userLogin(request : LoginRequest){
        loginView.showProgress()
        loginInteractor.performLogin(request)
    }
}