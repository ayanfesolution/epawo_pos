package com.epawo.custodian.fragment.betting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.epawo.custodian.R
import com.epawo.custodian.adapter.BettingProviderAdapter
import com.epawo.custodian.databinding.LayoutBettingFragmentBinding
import com.epawo.custodian.fragment.BaseFragment
import com.epawo.custodian.fragment.NavigationCommand
import com.epawo.custodian.interfaces.BettingProviderClickListener
import com.epawo.custodian.model.betting.response.BettingProviderResponse
import com.epawo.custodian.model.betting.response.BettingResponseModel
import com.epawo.custodian.utilities.AppPreferences
import com.topwise.cloudpos.aidl.AidlDeviceService

class BettingFragment : BaseFragment(), BettingContract.BettingView, BettingProviderClickListener{

    private var _binding: LayoutBettingFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter : BettingProviderPresenter
    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = LayoutBettingFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        presenter = BettingProviderPresenter(this)
        setListeners()
        loadBettingProviders()
    }

    private fun setListeners(){
        binding.imageView3.setOnClickListener { onBackButtonClicked() }
    }

    private fun onBackButtonClicked(){
        navigate(NavigationCommand.Back)
    }

    private fun loadBettingProviders(){
        val token = AppPreferences().getUserToken(mainActivity).toString()
        presenter.loadBettingProviders(token)
    }

    override fun showToast(message: String?) {
       toastShort(message)
    }

    override fun showProgress() {
       binding.progressBar11.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar11.visibility = View.GONE
    }

    override fun onLoadBettingProvider(response: BettingProviderResponse) {
        if(response != null  && response.responseModel.isNotEmpty()){
            val bettingList = response.responseModel
            val bettingAdapter = BettingProviderAdapter(mainActivity, bettingList,this)
            val layoutManager = GridLayoutManager(mainActivity, 3)
            binding.providerList.layoutManager = layoutManager
            binding.providerList.adapter = bettingAdapter
            binding.providerList.visibility = View.VISIBLE
        }
    }

    override fun onBettingProviderClicked(item: BettingResponseModel) {
        val providerCode = item.providerCode
        val providerName = item.name
        val providerId = item.providerId
        val bundle = Bundle()
        bundle.putString("code", providerCode)
        bundle.putString("name", providerName)
        bundle.putString("id", providerId)
        navigate(R.id.action_bettingFragment_to_bettingLookupFragment, bundle)

    }

}