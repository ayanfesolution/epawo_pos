package com.epawo.custodian.fragment.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epawo.custodian.R
import com.epawo.custodian.adapter.TransactionHistoryAdapter
import com.epawo.custodian.databinding.LayoutTransactionHistoryBinding
import com.epawo.custodian.fragment.BaseFragment
import com.epawo.custodian.fragment.NavigationCommand
import com.epawo.custodian.interfaces.TransactionClickListener
import com.epawo.custodian.model.transaction.response.TransactionResponseModel
import com.epawo.custodian.model.transaction.response.Transactions
import com.epawo.custodian.utilities.AppPreferences
import com.topwise.cloudpos.aidl.AidlDeviceService

class TransactionHistoryFragment : BaseFragment(), TransactionContract.TransactionView ,TransactionClickListener {

    private var _binding: LayoutTransactionHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var transactionAdapter : TransactionHistoryAdapter
    private lateinit var presenter : TransactionPresenter
    private var TOTAL_PAGES = 0
    private var CURRENT_PAGE = 0
    var isLoading = false
    lateinit var transactionlList : MutableList<Transactions>
    lateinit var transactionModel :TransactionResponseModel
    lateinit var token : String
    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutTransactionHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        token = AppPreferences().getUserToken(mainActivity).toString()
        setListeners()
        initPresenter()
        loadTransactionHistory()
        initScrollListener()
    }

    private fun initScrollListener(){
        binding.transactionList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if(!isLoading){
                    if(linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition()
                        == transactionlList.size - 1){
                        loadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMore(){
        CURRENT_PAGE++
        isLoading = true
        if(CURRENT_PAGE <= TOTAL_PAGES){
            transactionAdapter.notifyItemInserted(transactionlList.size - 1)
            val scrollPosition = transactionlList.size
            transactionAdapter.notifyItemRemoved(scrollPosition)
            presenter.loadMore(token,CURRENT_PAGE.toString())
        }
    }

    private fun setListeners(){
        binding.imageView3.setOnClickListener{ onBackButtonClick() }
    }

    private fun initPresenter(){
        presenter = TransactionPresenter(this)
    }

    private fun loadTransactionHistory(){
        token = AppPreferences().getUserToken(mainActivity).toString()
        presenter.loadTransaction(token,"1")
    }

    private fun onBackButtonClick(){
        navigate(NavigationCommand.Back)
    }

    override fun onTransactionClicked(item: Transactions) {
        val bundle = Bundle()
        bundle.putSerializable("transaction", item)
        navigate(R.id.action_transactionHistoryFragment_to_transactionDetailsFragment, bundle)
    }

    override fun showToast(message: String?) {
       toastShort(message)
    }

    override fun showProgress() {
        binding.progressBar8.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar8.visibility = View.GONE
    }

    override fun onSuccess(response: TransactionResponseModel) {
        transactionModel = response
        TOTAL_PAGES = transactionModel.totalPage
        CURRENT_PAGE = transactionModel.currentPage
        transactionlList = transactionModel.transactions
        if (transactionlList.isEmpty()) {
            binding.noTransaction.visibility = View.VISIBLE
        } else {
            transactionAdapter = TransactionHistoryAdapter(transactionlList, this, mainActivity)
            binding.transactionList.adapter = transactionAdapter
            binding.transactionList.addItemDecoration(
                DividerItemDecoration(mainActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
            binding.transactionList.visibility = View.VISIBLE
        }
    }

    override fun onLoadMoreSuccess(response: TransactionResponseModel) {
        isLoading = false
        val newItems = response.transactions as MutableList<Transactions>
        transactionlList.addAll(newItems)
        transactionAdapter.notifyDataSetChanged()
    }
}