package com.epawo.custodian.fragment.transactions

import android.os.Bundle
import android.os.RemoteException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epawo.custodian.BuildConfig
import com.epawo.custodian.databinding.LayoutTransactionDetailsFragmentBinding
import com.epawo.custodian.fragment.BaseFragment
import com.epawo.custodian.fragment.NavigationCommand
import com.epawo.custodian.model.transaction.response.Transactions
import com.epawo.custodian.utilities.AppPreferences
import com.epawo.custodian.utilities.UrlConstants
import com.epawo.custodian.utilities.Utility
import com.epawo.custodian.utilities.Utility.Companion.formatCurrency
import com.epawo.custodian.utilities.Utility.Companion.roundOffDecimal
import com.topwise.cloudpos.aidl.AidlDeviceService
import com.topwise.cloudpos.aidl.printer.AidlPrinter
import com.topwise.cloudpos.aidl.printer.AidlPrinterListener
import com.topwise.cloudpos.aidl.printer.Align
import com.topwise.cloudpos.aidl.printer.PrintTemplate
import com.topwise.cloudpos.aidl.printer.TextUnit
import java.util.LinkedHashMap

class TransactionDetailsFragment : BaseFragment() {

    private var _binding: LayoutTransactionDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var transactionBundle : Transactions
    private lateinit var transactionAmount : String
    private lateinit var transactionType : String
    private lateinit var transactionStatus : String
    private lateinit var transactionMethod : String
    private lateinit var transactionDate : String
    private lateinit var transactionReference : String
    private lateinit var previousBalances : String
    private lateinit var currentBalances : String
    private lateinit var transactionInfo : String
    lateinit var printerDev: AidlPrinter
    lateinit var valueMap: LinkedHashMap<String, String>

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {
        try {
            printerDev = AidlPrinter.Stub.asInterface(serviceManager!!.printer)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    var mListen: AidlPrinterListener = object : AidlPrinterListener.Stub() {
        @Throws(RemoteException::class)
        override fun onError(i: Int) {
            toastShort("Print error, error code: $i")
        }

        @Throws(RemoteException::class)
        override fun onPrintFinish() {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutTransactionDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        extractBundles()
        setListeners()
        setControls()
    }

    private fun extractBundles(){
        val bundle = arguments
        transactionBundle = bundle?.getSerializable("transaction") as Transactions
        transactionType = transactionBundle.transactionType.toString()
        transactionStatus = transactionBundle.transactionStatus.toString()
        transactionMethod = transactionBundle.transactionMethod.toString()
        transactionAmount = formatCurrency(transactionBundle.amount).toString()
        transactionDate = transactionBundle.transactionDate.toString()
        transactionReference = transactionBundle.transactionReferenceNo.toString()
        val prevBal = transactionBundle.previous
        val currBal = transactionBundle.current
        previousBalances = formatCurrency(roundOffDecimal(prevBal)).toString()
        currentBalances = formatCurrency(roundOffDecimal(currBal)).toString()
        transactionInfo = transactionBundle.fullName.toString()
    }

    private fun setControls(){
        binding.transactionDate.text = Utility.convertDateToUTCDate(transactionDate)
        binding.transactionAmount.text = transactionAmount
        binding.transactionStatus.text = transactionStatus
        binding.transactionType.text = transactionMethod
        binding.currentBalance.text = currentBalances
        binding.previousBalance.text = previousBalances
        binding.transactionReference.text = transactionReference
        binding.customerInfo.text = transactionInfo
    }

    private fun setListeners(){
        binding.imageView3.setOnClickListener{ onBackButtonClick() }
        binding.printReceipt.setOnClickListener { onPrintButtonClick() }
    }

    private fun getBundles(){
        valueMap = LinkedHashMap()
        //valueMap["Terminal ID: "] = AppPreferences().getTerminalId(mainActivity)!!
        valueMap["Customer Info: "] = transactionInfo
        valueMap["Status"] = transactionStatus
        valueMap["Date/Time : "] = Utility.convertDateToUTCDate(transactionDate)
        valueMap["Reference: "] = transactionReference
    }

    private fun onPrintButtonClick(){
        getBundles()
        printReceipt()
    }

    private fun onBackButtonClick(){
        navigate(NavigationCommand.Back)
    }

    private fun printReceipt(){

        try {
            val poweredBy = UrlConstants.POWERED_BY
            val businessName = AppPreferences().getCompanyName(mainActivity).toString()
            val businessAddress = AppPreferences().getAddress(mainActivity).toString()
            val agentEmail = AppPreferences().getEmail(mainActivity).toString()
            val phone = AppPreferences().getPhone(mainActivity).toString()
            //val bitmap =  getAppBitmap(mainActivity)
            val template = PrintTemplate.getInstance()
            template.init(mainActivity, null)
            template.clear()
            //template.add(ImageUnit(bitmap, 384, 125))
            template.add(1,TextUnit("Merchant Name :", TextUnit.TextSize.NORMAL, Align.LEFT).setBold(false),1, TextUnit(
                "$businessName", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(false))
            template.add(1,TextUnit("Address :", TextUnit.TextSize.NORMAL, Align.LEFT).setBold(false),1, TextUnit(
                "$businessAddress", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(false))
            template.add(1,TextUnit("Customer Care:", TextUnit.TextSize.NORMAL, Align.LEFT).setBold(false),1, TextUnit(
                "$phone", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(false))
            template.add(1,TextUnit("Email:", TextUnit.TextSize.NORMAL, Align.LEFT).setBold(false),1, TextUnit(
                "$agentEmail", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(false))
            template.add(TextUnit("................................................................."))
            template.add(TextUnit("REPRINT(${transactionMethod})", TextUnit.TextSize.LARGE, Align.CENTER).setBold(true))
            template.add(TextUnit("................................................................."))
            for ((key, value) in valueMap) {
                template.add(1,
                    TextUnit(
                        "$key",
                        TextUnit.TextSize.NORMAL,
                        Align.LEFT
                    ).setBold(false),1, TextUnit(
                        "$value",
                        TextUnit.TextSize.NORMAL,
                        Align.RIGHT
                    ).setBold(false)
                )
            }
            template.add(TextUnit("Amount $transactionAmount", TextUnit.TextSize.NORMAL, Align.LEFT).setBold(true))
            template.add(TextUnit(" "))
            template.add(TextUnit("***** $poweredBy *****", TextUnit.TextSize.NORMAL, Align.CENTER).setBold(true))
            template.add(TextUnit("App Version " + BuildConfig.VERSION_NAME, TextUnit.TextSize.NORMAL, Align.CENTER).setBold(true))
            template.add(TextUnit("\n\n\n\n"))
            printerDev.addRuiImage(template.printBitmap, 0)
            printerDev.printRuiQueue(mListen)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }
}