package com.epawo.egole.fragment.cashout

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.RemoteException
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.epawo.egole.BuildConfig
import com.epawo.egole.R
import com.epawo.egole.databinding.PrintFragmentLayoutBinding
import com.epawo.egole.fragment.BaseFragment
import com.epawo.egole.fragment.NavigationCommand
import com.epawo.egole.utilities.AppPreferences
import com.epawo.egole.utilities.UrlConstants
import com.epawo.egole.utilities.Utility
import com.topwise.cloudpos.aidl.AidlDeviceService
import com.topwise.cloudpos.aidl.printer.*

class PrintFragment : BaseFragment() {

    lateinit var printerDev: AidlPrinter
    var printCount = 1
    var amount = ""
    var status = ""
    var description = ""
    var responseCode = ""
    var rrn = ""
    var stan = ""
    var expiryDate = ""
    var cardNumber = ""
    var dateTime = ""
    var accountType = ""
    var terminalID = ""
    var cardType = ""
    var stats = ""
    var customerName = ""
    var ref = ""

    private var _binding: PrintFragmentLayoutBinding? = null
    private val binding get() = _binding!!

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

        _binding = PrintFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setListeners()
        getBundles()
    }

    private fun setListeners(){
        binding.backImage.setOnClickListener { onBackButtonClick() }
        binding.btnExit.setOnClickListener { onExitButtonClick() }
        binding.btnPrint.setOnClickListener{ onPrintButtonClick() }
    }

    private fun getBundles(){
        val bundle = arguments
        if(bundle != null){
            terminalID = if(!TextUtils.isEmpty(bundle.getString("TerminalId"))) bundle.getString("TerminalId")!! else ""
            cardNumber = if(!TextUtils.isEmpty(bundle.getString("cardNo"))) bundle.getString("cardNo")!! else ""
            cardType = if(!TextUtils.isEmpty(bundle.getString("cardType"))) bundle.getString("cardType")!! else ""
            expiryDate = if(!TextUtils.isEmpty(bundle.getString("expiry"))) bundle.getString("expiry")!! else ""
            rrn = if(!TextUtils.isEmpty(bundle.getString("rrn"))) bundle.getString("rrn")!! else ""
            stan = if(!TextUtils.isEmpty(bundle.getString("stan"))) bundle.getString("stan")!! else ""
            ref = if(!TextUtils.isEmpty(bundle.getString("ref"))) bundle.getString("ref")!! else ""
            accountType = if(!TextUtils.isEmpty(bundle.getString("accountType"))) bundle.getString("accountType")!! else ""
            responseCode = if(!TextUtils.isEmpty(bundle.getString("responseCode"))) bundle.getString("responseCode")!! else ""
            description = if(!TextUtils.isEmpty(bundle.getString("description"))) bundle.getString("description")!! else ""
            status = if(!TextUtils.isEmpty(bundle.getString("status"))) bundle.getString("status")!! else ""
            dateTime = if(!TextUtils.isEmpty(bundle.getString("date"))) bundle.getString("date")!! else ""
            amount = if(!TextUtils.isEmpty(bundle.getString("amount"))) bundle.getString("amount")!! else ""
            customerName = if(!TextUtils.isEmpty(bundle.getString("customerName"))) bundle.getString("customerName")!! else ""
            binding.consumeSucessText.text = if(!TextUtils.isEmpty(bundle.getString("status"))) bundle.getString("status")!!.toUpperCase() else ""
        }
    }

    private fun onPrintButtonClick(){
        if(printCount == 1){
            printCount += 1
            printData("CUSTOMER COPY")
        }else if(printCount == 2){
            printData("MERCHANT COPY")
            printCount +=1
        }
    }

    private fun printData(user: String) {
        val bitmap = BitmapFactory.decodeResource(mainActivity.resources, R.drawable.epawo_logo)
        try {
            val businessName = AppPreferences().getCompanyName(mainActivity).toString()
            val businessAddress = AppPreferences().getAddress(mainActivity).toString()
            val template = PrintTemplate.getInstance()
            template.init(mainActivity, null)
            template.clear()
            template.add(ImageUnit(bitmap, 384, 125))
            template.add(TextUnit("**** $user ****", TextUnit.TextSize.SMALL, Align.CENTER).setBold(true))
            template.add(1, TextUnit("MERCHANT NAME:", TextUnit.TextSize.NORMAL,Align.LEFT).setBold(false),1, TextUnit(
                "${businessName.toUpperCase()}", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(true))
            template.add(1, TextUnit("LOCATION:", TextUnit.TextSize.NORMAL,Align.LEFT).setBold(false),1, TextUnit(
                " ${businessAddress.toUpperCase()}", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(true))
            template.add(1, TextUnit("TERMINAL ID:", TextUnit.TextSize.NORMAL,Align.LEFT).setBold(false),1, TextUnit(
                "$terminalID", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(true))
            template.add(TextUnit("................................................................."))
            template.add(1, TextUnit("PAN:", TextUnit.TextSize.NORMAL,Align.LEFT).setBold(false),1, TextUnit(
                "$cardNumber", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(true))
            template.add(1, TextUnit("CARD TYPE:", TextUnit.TextSize.NORMAL,Align.LEFT).setBold(false),1, TextUnit(
                "$cardType", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(true))
            template.add(1, TextUnit("DATE/TIME:", TextUnit.TextSize.NORMAL,Align.LEFT).setBold(false),1, TextUnit(
                "$dateTime", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(true))
            template.add(1, TextUnit("AMOUNT:", TextUnit.TextSize.NORMAL,Align.LEFT).setBold(false),1, TextUnit(
                "${Utility.formatCurrency(amount.toDouble())}", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(true))
            template.add(1, TextUnit("CUSTOMER NAME:", TextUnit.TextSize.NORMAL,Align.LEFT).setBold(false),1, TextUnit(
                "$customerName", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(true))
            template.add(TextUnit("................................................................."))
            template.add(TextUnit("${status.toUpperCase()}", TextUnit.TextSize.LARGE, Align.CENTER).setBold(true))
            template.add(TextUnit("................................................................."))
            template.add(1, TextUnit("RESPONSE CODE:", TextUnit.TextSize.NORMAL,Align.LEFT).setBold(false),1, TextUnit(
                "$responseCode", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(true))
            template.add(1, TextUnit("AID:", TextUnit.TextSize.NORMAL,Align.LEFT).setBold(false),1, TextUnit(
                "A000000041010", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(true))
            template.add(1, TextUnit("RRN:", TextUnit.TextSize.NORMAL,Align.LEFT).setBold(false),1, TextUnit(
                rrn, TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(true))
            template.add(1, TextUnit("STAN:", TextUnit.TextSize.NORMAL,Align.LEFT).setBold(false),1, TextUnit(
                stan, TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(true))
            template.add(1, TextUnit("REFERENCE:", TextUnit.TextSize.NORMAL,Align.LEFT).setBold(false),1, TextUnit(
                ref, TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(true))
            template.add(1, TextUnit("Expiry DATE:", TextUnit.TextSize.NORMAL,Align.LEFT).setBold(false),1, TextUnit(
                "$expiryDate", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(true))
            template.add(1, TextUnit("AUTHORIZATION CODE:", TextUnit.TextSize.NORMAL,Align.LEFT).setBold(false),1, TextUnit(
                    "000000", TextUnit.TextSize.NORMAL, Align.RIGHT).setBold(true))
            template.add(TextUnit("................................................................."))
            template.add(TextUnit("App Version " + BuildConfig.VERSION_NAME, TextUnit.TextSize.SMALL, Align.CENTER).setBold(true))
            template.add(TextUnit("Thanks for using EPAWO POS", TextUnit.TextSize.SMALL, Align.CENTER).setBold(true))
            template.add(TextUnit("\n\n\n\n"))
            printerDev.addRuiImage(template.printBitmap, 0)
            printerDev.printRuiQueue(mListen)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    private fun onBackButtonClick(){
        navigate(NavigationCommand.To(actionPrintFragmentToHomeFragment()))
    }

    private fun onExitButtonClick(){
        navigate(NavigationCommand.To(actionPrintFragmentToHomeFragment()))
    }

    private fun actionPrintFragmentToHomeFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_printFragment_to_homeFragment)
    }
}