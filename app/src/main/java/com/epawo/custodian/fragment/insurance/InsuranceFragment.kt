package com.epawo.custodian.fragment.insurance

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epawo.custodian.R
import com.epawo.custodian.databinding.LayoutInsuranceFragmentBinding
import com.epawo.custodian.fragment.BaseFragment
import com.epawo.custodian.fragment.NavigationCommand
import com.epawo.custodian.model.insurance.InsuranceDetailResponse
import com.epawo.custodian.model.insurance.InsuranceDetailsRequest
import com.epawo.custodian.utilities.AppPreferences
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
        binding.imageView3.setOnClickListener { navigate(NavigationCommand.Back)}
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

        if (response.status == 200) {
            val bundle = Bundle()
            bundle.putString("bizUnit", "Custodian Insurance Payment")
           bundle.putString("insuredName", response.data.policy_holder.full_name)
            bundle.putString("insuredEmail", response.data.policy_holder.email_address.toString())
            bundle.putString("amount", response.data.payment_info.installment_payment.toString())
            bundle.putString("policyNumber", response.data.policy_number)
            bundle.putString("phone", response.data.policy_holder.mobile)
            bundle.putString("desc", response.data.policy_holder.customer_id)
            bundle.putString("3rd_party_id", response.extra_data.third_party_id)
            navigate(R.id.action_insuranceFragment_to_insuranceDetailFragment, bundle)
        } else {
            toastShort(response.message)
        }
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