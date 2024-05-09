package com.epawo.egole.fragment.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epawo.egole.R
import com.epawo.egole.databinding.LayoutInsuranceDetailFragmentBinding
import com.epawo.egole.fragment.BaseFragment
import com.epawo.egole.utilities.Utility
import com.topwise.cloudpos.aidl.AidlDeviceService

class InsuranceDetailFragment: BaseFragment() {
    private var _binding: LayoutInsuranceDetailFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var amount: String
    lateinit var bizName: String
    lateinit var email:String
    lateinit var name: String
    lateinit var phone: String
    lateinit var desc: String
    lateinit var policyNumber: String

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutInsuranceDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        extractBundles()
        setControls()
        setListeners()
    }

    private fun extractBundles(){
        val bundle = arguments
        amount = bundle?.getString("amount").toString()
        name = bundle?.getString("insuredName").toString()
        email = bundle?.getString("insuredEmail").toString()
        bizName = bundle?.getString("bizUnit").toString()
        policyNumber = bundle?.getString("policyNumber").toString()
        phone = bundle?.getString("phone").toString()
        desc = bundle?.getString("desc").toString()
    }

    private fun setControls(){
        binding.amount.text = Utility.formatCurrency(amount.toDouble())
        binding.insuredName.text = name
        binding.businessUnit.text = bizName
        binding.insuredEmail.text = email
    }

    private fun setListeners(){
        binding.proceedButton.setOnClickListener { onContinueButtonClick() }
    }

    private fun onContinueButtonClick(){
        val bundle = Bundle()
        bundle.putString("amount",amount)
        bundle.putString("insuredName", name)
        bundle.putString("insuredEmail", email)
        bundle.putString("bizname", bizName)
        bundle.putString("policyNumber", policyNumber)
        bundle.putString("phone", phone)
        bundle.putString("desc", desc)
        navigate(R.id.action_insuranceDetailFragment_to_insuranceCashout, bundle)
    }

}