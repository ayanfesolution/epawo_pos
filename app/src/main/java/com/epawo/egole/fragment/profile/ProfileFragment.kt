package com.epawo.egole.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.epawo.egole.R
import com.epawo.egole.databinding.LayoutLoginFragmentBinding
import com.epawo.egole.databinding.LayoutProfileFragmentBinding
import com.epawo.egole.fragment.BaseFragment
import com.epawo.egole.fragment.NavigationCommand
import com.epawo.egole.fragment.login.LoginPresenter
import com.epawo.egole.utilities.AppPreferences
import com.topwise.cloudpos.aidl.AidlDeviceService

class ProfileFragment : BaseFragment() {

    private var _binding: LayoutProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        setListeners()
        setControls()
    }

    private fun setListeners(){
        binding.imageView3.setOnClickListener { onBackButtonClick() }
        binding.editProfile.setOnClickListener { onEditProfileClick() }
        binding.resetPassword.setOnClickListener { onResetPasswordClick() }
        binding.transactionPin.setOnClickListener { onSetTransactionPinClick() }
    }

    private fun setControls(){
        binding.businessName.text = AppPreferences().getCompanyName(mainActivity)
    }

    private fun onBackButtonClick(){
        navigate(NavigationCommand.Back)
    }

    private fun onEditProfileClick(){
        navigate(NavigationCommand.To(actionProfileFragmentToEditProfileFragment()))
    }
    private fun onResetPasswordClick(){}
    private fun onSetTransactionPinClick(){}
    private fun actionProfileFragmentToEditProfileFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_profileFragment_to_editProfileFragment)
    }
    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}