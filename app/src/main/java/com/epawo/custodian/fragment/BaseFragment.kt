package com.epawo.custodian.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.epawo.custodian.activity.MainActivity
import com.epawo.custodian.extensions.mayNavigate
import com.topwise.cloudpos.aidl.AidlDeviceService

abstract class BaseFragment : Fragment()  {
    protected lateinit var mainActivity: MainActivity
    var flag = false

    val TOPWISE_SERVICE_ACTION = "topwise_cloudpos_device_service"
    private val TAG = "TPW-BaseTestActivity"

    private val conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, serviceBinder: IBinder) {
            Log.d(TAG, "aidlService服务连接成功")
            if (serviceBinder != null) {    //绑定成功
                val serviceManager = AidlDeviceService.Stub.asInterface(serviceBinder)
                onDeviceConnected(serviceManager)
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.d(TAG, "AidlService服务断开了")
        }
    }

    open fun bindService() {
        Log.d(TAG, "bindService: enter")
        val intents = Intent()
        intents.action = TOPWISE_SERVICE_ACTION
        val eintent = Intent(createExplicitFromImplicitIntent(mainActivity, intents))
        flag = requireActivity().bindService(eintent, conn, Context.BIND_AUTO_CREATE)
        Log.d(TAG, "bindService: $flag")
        if (flag) {
            Log.d(TAG, "服务绑定成功")
        } else {
            Log.d(TAG, "服务绑定失败")
        }
        Log.d(TAG, "bindService: exit")
    }

    open fun createExplicitFromImplicitIntent(context: Context, implicitIntent: Intent?): Intent? {
        // Retrieve all services that can match the given intent
        val pm = context.packageManager
        val resolveInfo = pm.queryIntentServices(
            implicitIntent!!, 0
        )

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size != 1) {
            return null
        }

        // Get component info and create ComponentName
        val serviceInfo = resolveInfo[0]
        val packageName = serviceInfo.serviceInfo.packageName
        val className = serviceInfo.serviceInfo.name
        val component = ComponentName(packageName, className)

        // Create a new intent. Use the old one for extras and such reuse
        val explicitIntent = Intent(implicitIntent)

        // Set the component to be explicit
        explicitIntent.component = component
        return explicitIntent
    }

    override fun onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy()
        if(flag){
            requireActivity().unbindService(conn)
        }
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
        bindService()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = requireActivity() as MainActivity
    }

    protected fun navigate(command : NavigationCommand) : Any{
        return when(command){
            is NavigationCommand.To ->{
                try{
                    this.findNavController().navigate(command.directions,
                        FragmentNavigatorExtras()
                    )
                }catch(e: Exception){

                }
            }
            is NavigationCommand.Back -> findNavController().navigateUp()
        }
    }

    abstract fun onDeviceConnected(serviceManager: AidlDeviceService?)

    override fun onDestroyView() {
        super.onDestroyView()
    }

    protected fun navigate(action: Int, bundle : Bundle?){
        if(mayNavigate()) findNavController().navigate(action, bundle)
    }

    protected fun disableBackPressed(){
        mainActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, true){
            println("disable back press")
        }
    }

    protected fun hideSoftKeyboard(view : View){
        val imm = mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }

    fun toastLong(message : String?){
       // if(message.isNullOrEmpty()) return
        Toast.makeText(mainActivity, message, Toast.LENGTH_LONG).show()
    }

    fun toastShort(message : String?){
        //if(message.isNullOrEmpty()) return
        Toast.makeText(mainActivity, message, Toast.LENGTH_SHORT).show()
    }

}

sealed class NavigationCommand{
    data class To(val directions : NavDirections) : NavigationCommand()
    object Back : NavigationCommand()
}