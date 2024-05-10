package com.epawo.custodian.fragment.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epawo.custodian.adapter.TicketListAdapter
import com.epawo.custodian.databinding.LayoutTicketListFragmentBinding
import com.epawo.custodian.fragment.BaseFragment
import com.epawo.custodian.fragment.NavigationCommand
import com.epawo.custodian.model.ticket.TicketData
import com.epawo.custodian.model.ticket.TicketResponseModel
import com.epawo.custodian.utilities.AppPreferences
import com.epawo.custodian.utilities.Utility
import com.topwise.cloudpos.aidl.AidlDeviceService

class TicketListFragment : BaseFragment(), TicketListContract.TicketListView {

    private var _binding: LayoutTicketListFragmentBinding? = null
    private val binding get() = _binding!!
    private val ticketItems = mutableListOf<TicketData>()
    private lateinit var token : String
    private lateinit var presenter : TicketListPresenter

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutTicketListFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        token = AppPreferences().getUserToken(mainActivity).toString()
        presenter = TicketListPresenter(this)
        setListeners()
        loadTickets()

    }

    private fun loadTickets(){
        val walletId = AppPreferences().getWalletId(mainActivity).toString()
        val startDate =  Utility.getOneMonthDate()
        val endDate = Utility.getPresentDates()
        presenter.loadTickets(token,walletId,startDate,endDate)
    }

    private fun setListeners(){
        binding.imageView3.setOnClickListener { onBackPressed() }
    }

    private fun onBackPressed(){
        navigate(NavigationCommand.Back)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showToast(message: String?) {
        toastShort(message)
    }

    override fun showProgress() {
       binding.progressBar10.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar10.visibility = View.GONE
    }

    override fun onSuccess(response: TicketResponseModel) {
       if(response.ticket == null || response.ticket.isEmpty()){
           binding.textView7.visibility = View.VISIBLE
       }else{
           val adapter = TicketListAdapter(mainActivity, response.ticket)
           binding.ticketRecycler.adapter = adapter
           binding.ticketRecycler.visibility = View.VISIBLE
       }
    }
}