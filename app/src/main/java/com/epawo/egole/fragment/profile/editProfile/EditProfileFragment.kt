package com.epawo.egole.fragment.profile.editProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epawo.egole.databinding.LayoutEditProfileFragmentBinding
import com.epawo.egole.fragment.BaseFragment
import com.epawo.egole.fragment.NavigationCommand
import com.topwise.cloudpos.aidl.AidlDeviceService

class EditProfileFragment : BaseFragment() {

    private var _binding: LayoutEditProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutEditProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        setListeners()
    }

    private fun setListeners(){
        binding.imageView3.setOnClickListener { onBackButtonClick() }
        binding.continueButton.setOnClickListener { onContinueButton() }
    }

    private fun onContinueButton(){}
    private fun onBackButtonClick(){
        navigate(NavigationCommand.Back)
    }
    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}