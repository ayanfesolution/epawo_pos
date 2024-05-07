package com.epawo.egole.fragment.login

import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.epawo.egole.BuildConfig
import com.epawo.egole.R
import com.epawo.egole.databinding.LayoutLoginFragmentBinding
import com.epawo.egole.fragment.BaseFragment
import com.epawo.egole.fragment.NavigationCommand
import com.epawo.egole.model.login.request.LoginRequest
import com.epawo.egole.model.login.response.LoginResponse
import com.epawo.egole.model.login.response.Wallets
import com.epawo.egole.utilities.AppConstants
import com.epawo.egole.utilities.AppPreferences
import com.google.gson.Gson
import com.topwise.cloudpos.aidl.AidlDeviceService
import com.topwise.library.DeviceManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LoginFragment : BaseFragment(), LoginContract.LoginView {

    private var _binding: LayoutLoginFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginPresenter : LoginPresenter
    private var showPassword = false
    private lateinit var selectedWallet : Wallets

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutLoginFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        loginPresenter = LoginPresenter(this)
       // setControls()
        setListeners()
    }

    private fun setListeners(){
        binding.forgotPassword.setOnClickListener{ onForgotPasswordLayoutClick() }
        binding.loginButton.setOnClickListener { onLoginButtonClick() }
        binding.showPassBtn.setOnClickListener{ onShowPasswordClick() }
    }

    private fun onShowPasswordClick(){
        if(showPassword){
            showPassword = false
            binding.password.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.showPassBtn.setImageResource(R.drawable.hide_password)
        }else{
            showPassword = true
            binding.password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.showPassBtn.setImageResource(R.drawable.show_password)
        }
    }

    private fun setControls(){
        binding.username.setText("08033396046")
        binding.password.setText("A@33word")
    }

    private fun onLoginButtonClick(){
        val username = binding.username.text.toString()
        val password = binding.password.text.toString()
        val versionCode = BuildConfig.VERSION_CODE
        val deviceSerialNumber = DeviceManager.getInstance().systemManager.serialNo //"P260300469621" //DeviceManager.getInstance().systemManager.serialNo  //"P260300469621"
        val userAgent = "Android/$versionCode/$deviceSerialNumber"
        if(validateControls(username,password)){
            val loginRequest = LoginRequest(username,password, AppConstants.DEVICE_TYPE,
                AppConstants.DEVICE_NAME,userAgent, AppConstants.BRAND_NAME, AppConstants.BRAND, deviceSerialNumber) //P260301173630
            val gson = Gson()
            val stringRequest = gson.toJson(loginRequest)
            loginPresenter.userLogin(loginRequest)
        }
    }

    private fun validateControls(username : String, password : String) : Boolean{
        if(TextUtils.isEmpty(username)){
            toastShort(getString(R.string.username_error))
            return false
        }else if(TextUtils.isEmpty(password)){
            toastShort(getString(R.string.password_error))
            return false
        }

        return true
    }

    private fun onForgotPasswordLayoutClick(){
        navigate(NavigationCommand.To(actionLoginFragmentToResetPasswordFragment()))
    }

    private fun actionLoginFragmentToResetPasswordFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_loginFragment_to_resetPasswordFragment)
    }

    private fun actionLoginFragmentToHomeFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_loginFragment_to_homeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showToast(message: String?) {
        toastShort(message)
    }

    override fun showProgress() {
        binding.loginProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.loginProgressBar.visibility = View.GONE
    }

    override fun onLoginSuccess(response: LoginResponse) {
        if(response.profileViews == null){
            toastShort("Invalid username or password")
        }else{
            val simpleDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val currentDate = simpleDate.format(Calendar.getInstance().time)
            val agentWallet = response.profileViews.wallets

            AppPreferences().setUserToken(mainActivity, response.token)
            AppPreferences().setUserId(mainActivity, response.userid)
            AppPreferences().setTerminalId(mainActivity, response.profileViews.terminalId.toString())
            AppPreferences().setAddress(mainActivity, response.profileViews.address.toString())
            AppPreferences().setPhone(mainActivity,response.profileViews.phoneNumber)
            AppPreferences().setEmail(mainActivity,response.profileViews.email)
            AppPreferences().setWalletId(mainActivity, response.profileViews.walletId.toString())
            AppPreferences().setWalletBalance(mainActivity, response.profileViews.walletBalance.toString())
            AppPreferences().setWalletAccountNumber(mainActivity, response.profileViews.mainWallet)
            AppPreferences().setCompanyName(mainActivity, response.profileViews.companyName)
            AppPreferences().setLoginDate(mainActivity, currentDate.toString())
            AppPreferences().setBusinessName(mainActivity, response.profileViews.businessName.toString())
            AppPreferences().setBusinessAddress(mainActivity, response.profileViews.businessAddress.toString())

            val walletList = Gson().toJson(agentWallet)
            AppPreferences().setWalletList(mainActivity, walletList)
            navigate(NavigationCommand.To(actionLoginFragmentToHomeFragment()))
        }
    }
}