package com.epawo.custodian.fragment.reset_password

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epawo.custodian.R
import com.epawo.custodian.databinding.LayoutResetPasswordFragmentBinding
import com.epawo.custodian.fragment.BaseFragment
import com.epawo.custodian.model.forgot_password.request.ForgotPasswordRequest
import com.epawo.custodian.model.forgot_password.response.ForgotPasswordResponse
import com.epawo.custodian.utilities.AppPreferences
import com.epawo.custodian.utilities.Utility
import com.topwise.cloudpos.aidl.AidlDeviceService

class ResetPasswordFragment : BaseFragment(), ForgotPasswordContract.ForgotPasswordView {

    private var _binding: LayoutResetPasswordFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter : ForgotPasswordPresenter

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutResetPasswordFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        setListeners()
        initPresenter()
    }

    private fun setListeners(){
        binding.resetButton.setOnClickListener { onResetButtonClick() }
    }

    private fun initPresenter(){
        presenter = ForgotPasswordPresenter(this)
    }

    private fun onResetButtonClick(){
        val emailAddress = binding.emailAddress.text.toString().trim()
        val token = AppPreferences().getUserToken(mainActivity).toString()
        if(validateControls(emailAddress)){
            val request = ForgotPasswordRequest(emailAddress)
            presenter.forgotPassword(token, request)
        }
    }

    private fun validateControls(email : String) : Boolean{
        if(TextUtils.isEmpty(email)){
            toastShort(getString(R.string.please_input_your_email_address))
            return false
        }else if(!Utility.isValidEmail(email)){
            toastShort(getString(R.string.input_a_valid_email_address))
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showToast(message: String?) {
        toastShort(message)
    }

    override fun showProgress() {
       binding.progressBar9.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar9.visibility = View.GONE
    }

    override fun forgotPasswordSuccess(cashout: ForgotPasswordResponse) {
        toastShort(cashout.message)

    }
}