package com.epawo.egole.fragment.airtime.data

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.epawo.egole.R
import com.epawo.egole.databinding.LayoutAirtimeDateFragmentBinding
import com.epawo.egole.fragment.BaseFragment
import com.epawo.egole.fragment.NavigationCommand
import com.epawo.egole.fragment.airtime.airtime.AirtimeCategoriesContract
import com.epawo.egole.fragment.airtime.airtime.AirtimeCategoriesPresenter
import com.epawo.egole.model.airtime.response.AirtimeDataResponse
import com.epawo.egole.model.airtime.response.DataProducts
import com.epawo.egole.model.category.response.CategoryResponse
import com.epawo.egole.utilities.AppPreferences
import com.epawo.egole.utilities.UrlConstants.Companion.CURRENCY_NAIRA
import com.topwise.cloudpos.aidl.AidlDeviceService

class DataRechargeFragment : BaseFragment(), DataRechargeContract.DataRechargeView,
    AirtimeCategoriesContract.AirtimeCategoriesView {

    private var _binding: LayoutAirtimeDateFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter : DataRechargePresenter
    private lateinit var categoriesPresenter : AirtimeCategoriesPresenter
    private lateinit var selectedProvider : String
    private lateinit var selectedProviderCode : String
    private lateinit var userToken : String
    private lateinit var dataPrice : String
    private lateinit var dataBundleCode : String
    private lateinit var dataAllowance : String
    private lateinit var dataValidity : String
    private lateinit var serviceCode : String
    private lateinit var productModeID : String
    private lateinit var bundleList : List<DataProducts>
    private lateinit var mtnCode : String
    private lateinit var gloCode : String
    private lateinit var airtelCode : String
    private lateinit var nineMobileCode : String
    private lateinit var type : String
    private lateinit var surauth : String

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = LayoutAirtimeDateFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        presenter = DataRechargePresenter(this)
        userToken = AppPreferences().getUserToken(mainActivity).toString()
        categoriesPresenter = AirtimeCategoriesPresenter(this)
        setListeners()
        loadProviders()
        loadCategories()
    }

    private fun loadCategories(){
        categoriesPresenter.loadCategories(userToken)
    }

    private fun setListeners(){
        binding.imageView3.setOnClickListener { onBackButtonClick() }
        binding.continueButton.setOnClickListener { onContinueButtonClick() }
        binding.providerFilledExposedDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                selectedProvider = binding.providerFilledExposedDropdown.text.toString()
                getSelectedProviderCode(selectedProvider)
                loadDataBundles()
            }

        binding.bundleFilledExposedDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                getBundlesDetails(position)
            }
    }

    private fun loadProviders(){
        val providerList = resources.getStringArray(R.array.provider)
        val arrayAdapter = ArrayAdapter(mainActivity, R.layout.dropdown_menu,providerList)
        binding.providerFilledExposedDropdown.setAdapter(arrayAdapter)
    }

    private fun onBackButtonClick(){
        navigate(NavigationCommand.Back)
    }

    private fun onContinueButtonClick(){
        val phone = binding.phoneNumberField.text.toString()
        if(validateControls(phone, selectedProvider,dataPrice)){
            val bundle = Bundle()
            bundle.putString("phone", phone)
            bundle.putString("provider", selectedProvider)
            bundle.putString("providerCode", selectedProviderCode)
            bundle.putString("bundlePrice", dataPrice)
            bundle.putString("dataBundleCode",dataBundleCode)
            bundle.putString("productModeID",productModeID)
           // bundle.putString("serviceCode", serviceCode)
            bundle.putString("validity", dataValidity)
            bundle.putString("allowance", dataAllowance)
            bundle.putString("type",type)
            bundle.putString("surauth",surauth)
            navigate(R.id.action_dataRechargeFragment_to_dataRechargeDetailsFragment, bundle)
        }
    }

    private fun validateControls(phone : String, provider : String, bundle : String) : Boolean{
        if(TextUtils.isEmpty(phone)){
            toastShort("Please input your phone number")
            return false
        }else if(phone.length < 11 || phone.length > 11){
            toastShort("Please input a valid phone number")
            return false
        }else if(TextUtils.isEmpty(provider)){
            toastShort("Please select your provider")
            return false
        }else if(TextUtils.isEmpty(bundle)){
            toastShort("Please select a data plan")
            return false
        }

        return true
    }

    private fun loadDataBundles(){
        presenter.loadDataBundles(userToken,selectedProvider)
    }

    private fun getBundlesDetails(position : Int){
        val item = bundleList[position]
        dataPrice = item.price.toString()
        dataBundleCode = item.code
        dataAllowance = item.allowance
        dataValidity = item.validity
    }

    private fun getSelectedProviderCode(type : String){
        when (type) {
            "MTN" -> {
                selectedProviderCode = mtnCode
            }
            "AIRTEL" -> {
                selectedProviderCode = airtelCode
            }
            "GLO" -> {
                selectedProviderCode = gloCode
            }
            "9MOBILE" -> {
                selectedProviderCode = nineMobileCode
            }
        }
    }

    override fun showToast(message: String?) {
       toastShort(message)
    }

    override fun showProgress() {
        binding.progressBar4.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar4.visibility = View.GONE
    }

    override fun onSuccess(response: List<CategoryResponse>) {

        if(response.isNotEmpty()) {
            for (item in response) {
                if(item.categoryName == "Data"){
                    val product = item.products
                    for(i in product){
                        when (i.productName) {
                            "MTN" -> {
                                mtnCode = i.productId.toString()
                            }
                            "GLO" -> {
                                gloCode = i.productId.toString()
                            }
                            "Airtel" -> {
                                airtelCode = i.productId.toString()
                            }
                            "9Mobile" -> {
                                nineMobileCode = i.productId.toString()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onLoadBundles(response: AirtimeDataResponse) {
        if(response?.product?.isNotEmpty() == true){
           // serviceCode = response.serviceCode.toString()
            productModeID = response.productModeID.toString()
            type = response.type.toString()
            surauth = response.surauth.toString()
            bundleList = response?.product!!
            val providerName = ArrayList<String>()
            for (i in bundleList){
                providerName.add(i.allowance + " - " + i.validity + " - " + CURRENCY_NAIRA + i.price)
            }
            val arrayAdapter = ArrayAdapter(mainActivity, R.layout.dropdown_menu,providerName)
            binding.bundleFilledExposedDropdown.setAdapter(arrayAdapter)
        }else{
            toastShort(response.message)
        }
    }


}