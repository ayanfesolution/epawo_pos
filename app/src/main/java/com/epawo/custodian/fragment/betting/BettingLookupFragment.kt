package com.epawo.custodian.fragment.betting

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epawo.custodian.databinding.LayoutBettingLookupFragmentBinding
import com.epawo.custodian.fragment.BaseFragment
import com.epawo.custodian.fragment.NavigationCommand
import com.epawo.custodian.model.betting.response.BettingProviderResponse
import com.topwise.cloudpos.aidl.AidlDeviceService

class BettingLookupFragment : BaseFragment(), BettingContract.BettingView {

    private var _binding: LayoutBettingLookupFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var providerCode : String
    private lateinit var providerName : String

    private lateinit var presenter : BettingProviderPresenter

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = LayoutBettingLookupFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        presenter = BettingProviderPresenter(this)
        extractBundles()
        setListeners()
    }

    private fun extractBundles(){
        val bundle = arguments
        providerCode = bundle?.getString("code").toString()
        providerName = bundle?.getString("name").toString()
    }

    private fun setListeners(){
        binding.imageView3.setOnClickListener { onBackButtonClick() }
        binding.continueButton.setOnClickListener { onContinueButtonClick() }
    }

    private fun onContinueButtonClick(){
        val subscriberId = binding.subscriberField.text.toString().trim()
        val amount = binding.amount.text.toString().trim()
        if(validateControls(subscriberId,amount)){

        }
    }

    private fun validateControls(subscriberId : String, amount : String) : Boolean{
        if(TextUtils.isEmpty(subscriberId)){
            toastShort("Please input subscriber id")
            return false
        }else if(TextUtils.isEmpty(amount)){
            toastShort("Please input amount")
            return false
        }

        return true
    }

    private fun onBackButtonClick(){
        navigate(NavigationCommand.Back)
    }

    override fun showToast(message: String?) {

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun onLoadBettingProvider(response: BettingProviderResponse) {}
}