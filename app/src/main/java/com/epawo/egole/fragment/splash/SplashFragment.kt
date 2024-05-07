package com.epawo.egole.fragment.splash

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.epawo.egole.R
import com.epawo.egole.databinding.LayoutSplashFragmentBinding
import com.epawo.egole.fragment.BaseFragment
import com.epawo.egole.fragment.NavigationCommand
import com.epawo.egole.utilities.AppPreferences
import com.topwise.cloudpos.aidl.AidlDeviceService
import java.text.SimpleDateFormat
import java.util.Calendar

class SplashFragment : BaseFragment() {

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    private var _binding: LayoutSplashFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutSplashFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        setControls()
    }

    private fun setControls(){
        handleSplashDelay()
    }

    private fun handleSplashDelay() {
        if (binding.backgroundImage != null) {
            binding.backgroundImage.animation = AnimationUtils.loadAnimation(mainActivity, R.anim.splash_image_anim)
        }
        Handler().postDelayed({
            if(!isDateNextDay()){
                navigate(NavigationCommand.To(actionSplashFragmentToLoginFragment()))
            }else{
                navigate(NavigationCommand.To(actionSplashFragmentToHomeFragment()))
            }

        }, SPLASH_TIME_OUT.toLong())
    }

    private fun isDateNextDay() : Boolean{
        val oldDate = AppPreferences().getLoginDate(mainActivity).toString()
        return if(TextUtils.isEmpty(oldDate)){
            false
        }else{
            val simpleDate = SimpleDateFormat("yyyy/MM/dd")
            val currentDate = simpleDate.format(Calendar.getInstance().time)
            currentDate.equals(simpleDate.format(simpleDate.parse(oldDate)))
        }
    }

    private fun actionSplashFragmentToLoginFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_splashFragment_to_loginFragment)
    }

    private fun actionSplashFragmentToHomeFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_splashFragment_to_homeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val SPLASH_TIME_OUT = 3000
    }
}