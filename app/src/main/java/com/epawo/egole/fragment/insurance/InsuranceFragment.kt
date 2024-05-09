package com.epawo.egole.fragment.insurance

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.epawo.egole.R
import com.epawo.egole.databinding.LayoutInsuranceFragmentBinding
import com.epawo.egole.fragment.BaseFragment
import com.epawo.egole.model.insurance.InsuranceDetailResponse
import com.epawo.egole.model.insurance.InsuranceDetailsRequest
import com.epawo.egole.utilities.AppPreferences
import com.google.gson.Gson
import com.topwise.cloudpos.aidl.AidlDeviceService

class InsuranceFragment : BaseFragment(), InsurancePolicyContract.InsurancePolicyView {

    private var _binding: LayoutInsuranceFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter :InsurancePresenter
    private lateinit var userToken : String
    private lateinit var policyNumber: String

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutInsuranceFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        presenter = InsurancePresenter(this)
        userToken = AppPreferences().getUserToken(mainActivity).toString()
        setListeners()
    }


    private fun setListeners(){
        binding.continueButton.setOnClickListener { onContinueButtonClick() }
    }

    private fun onContinueButtonClick(){
        policyNumber = binding.policyField.text.toString()
        if(validateControls(policyNumber)){
            val request = InsuranceDetailsRequest(policyNumber)
            val gson = Gson()
            val requestString = gson.toJson(request)
            presenter.getInsuranceDetails(userToken, request)
        }
    }

    private fun validateControls(policyNumber : String) : Boolean{
        if(TextUtils.isEmpty(policyNumber)){
            toastShort("Please input your policy number")
            return false
        }

        return true
    }

    override fun showToast(message: String?) {
        toastShort(message)
    }

    override fun showProgress() {
        binding.progressBar12.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar12.visibility = View.GONE
    }

    override fun onSuccess(response: InsuranceDetailResponse) {
        val bundle = Bundle()
        bundle.putString("bizUnit",response.data.bizUnitField)
        bundle.putString("insuredName", response.data.insuredNameField)
        bundle.putString("insuredEmail", response.data.insuredEmailField)
        bundle.putString("amount", response.data.instPremiumField.toString())
        bundle.putString("policyNumber", policyNumber)
        bundle.putString("phone", response.data.insuredTelNumField)
        bundle.putString("desc", response.data.agenctNameField)
        navigate(R.id.action_insuranceFragment_to_insuranceDetailFragment, bundle)
//        bundle.putString("providerCode", providerCode)
//        bundle.putString("decoderNumber", cableNumber)
//        bundle.putString("serviceCode", serviceCode)
//        bundle.putString("providerName", providerName)
//        bundle.putString("providerCodeType", response.responseModel.type)
//        bundle.putString("productId", response.productID.toString())
//        bundle.putString("surauth", response.responseModel.surauth)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}