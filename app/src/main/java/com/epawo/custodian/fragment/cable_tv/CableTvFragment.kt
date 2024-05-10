package com.epawo.custodian.fragment.cable_tv

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.epawo.custodian.R
import com.epawo.custodian.databinding.LayoutCableTvFragmentBinding
import com.epawo.custodian.fragment.BaseFragment
import com.epawo.custodian.fragment.NavigationCommand
import com.epawo.custodian.model.cable.request.CableLookupRequest
import com.epawo.custodian.model.cable.request.CableProviderPackRequest
import com.epawo.custodian.model.cable.response.*
import com.epawo.custodian.utilities.AppPreferences
import com.epawo.custodian.utilities.UrlConstants
import com.google.gson.Gson
import com.topwise.cloudpos.aidl.AidlDeviceService

class CableTvFragment : BaseFragment(), CableProviderContract.CableProviderView{

    private var _binding: LayoutCableTvFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter : CableProviderPresenter
    private lateinit var userToken : String
    private lateinit var cableProviderList : List<CableProviderModel>
    private lateinit var cablePackList : List<CablePack>
    private lateinit var providerCode : String
    private lateinit var providerName : String
    private lateinit var serviceCode : String
    private lateinit var bundleAmount : String
    private lateinit var bundleName : String
    private lateinit var productCode : String
    private lateinit var cableNumber : String
    private lateinit var providerCodeType : String

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutCableTvFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        presenter = CableProviderPresenter(this)
        userToken = AppPreferences().getUserToken(mainActivity).toString()
        loadCableProviders()
        setListeners()
    }

    private fun setListeners(){
        binding.providerFilledExposedDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                getProviderDetails(position)
                loadProviderBundles()
            }

        binding.filledExposedDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                getBundleDetails(position)
            }

        binding.imageView3.setOnClickListener { onBackButtonClick() }
        binding.continueButton.setOnClickListener { onContinueButtonClick() }
    }
    private fun onContinueButtonClick(){
        cableNumber = binding.decoderNumberField.text.toString()
        if(validateControls(cableNumber, providerName,bundleName)){
            val request = CableLookupRequest(providerCode, cableNumber)
            binding.scrollView.scrollTo(0,0)
            val gson = Gson()
            val requestString = gson.toJson(request)
            presenter.loadCustomerDetails(userToken, request)
        }
    }

    private fun validateControls(cableNumber : String, providerName : String, cableBundle : String) : Boolean{
        if(TextUtils.isEmpty(cableNumber)){
            toastShort("Please input your decoder number")
            return false
        }else if(TextUtils.isEmpty(providerName)){
            toastShort("Please select your cable provider")
            return false
        }else if(TextUtils.isEmpty(cableBundle)){
            toastShort("Please select your cable bundle")
            return false
        }

        return true
    }

    private fun onBackButtonClick(){
        navigate(NavigationCommand.Back)
    }

    private fun getProviderDetails(position : Int){
        val data = cableProviderList[position]
        providerCode = data.providerCode
        providerName = data.name
        serviceCode = data.serviceCode
    }

    private fun getBundleDetails(position: Int){
        val data = cablePackList[position]
        bundleAmount = data.amount.toString()
        productCode = data.code
        bundleName = data.name
    }

    private fun loadProviderBundles(){
        val request = CableProviderPackRequest(serviceCode,providerCode)
        presenter.loadProviderBundles(userToken, request)
    }

    private fun loadCableProviders(){
        presenter.loadCableProvider(userToken)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showToast(message: String?) {
        toastShort(message)
    }

    override fun showProgress() {
        binding.progressBar5.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar5.visibility = View.GONE
    }

    override fun onSuccess(response: CableProviderResponse) {
        val providerName = ArrayList<String>()
        cableProviderList = response.responseModel
        for (i in response.responseModel){
            providerName.add(i.name)
        }
        val arrayAdapter = ArrayAdapter(mainActivity, R.layout.dropdown_menu,providerName)
        binding.providerFilledExposedDropdown.setAdapter(arrayAdapter)
    }

    override fun onLoadBundlesSuccess(response: CableProviderPackResponse) {
        val providerName = ArrayList<String>()
        cablePackList = response.responseModel
        for (i in response.responseModel){
            providerName.add(i.name + " - " + UrlConstants.CURRENCY_NAIRA + i.amount)
        }
        val arrayAdapter = ArrayAdapter(mainActivity, R.layout.dropdown_menu,providerName)
        binding.filledExposedDropdown.setAdapter(arrayAdapter)
    }

    override fun onLookUpSuccessful(response: CableLookupResponse) {
        val bundle = Bundle()
        bundle.putString("bundleName",bundleName)
        bundle.putString("productCode", productCode)
        bundle.putString("customerName", response.responseModel.name)
        bundle.putString("amount", bundleAmount)
        bundle.putString("providerCode", providerCode)
        bundle.putString("decoderNumber", cableNumber)
        bundle.putString("serviceCode", serviceCode)
        bundle.putString("providerName", providerName)
        bundle.putString("providerCodeType", response.responseModel.type)
        bundle.putString("productId", response.productID.toString())
        bundle.putString("surauth", response.responseModel.surauth)
        navigate(R.id.action_cableTvFragment_to_cablePaymentDetailsFragment, bundle)
    }
}