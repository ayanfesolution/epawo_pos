package com.epawo.egole.extensions

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.epawo.egole.R
import com.epawo.egole.activity.MainActivity
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.button.MaterialButton

fun Fragment.mayNavigate() : Boolean{

    val navController = findNavController()
    val destinationIdInNavController = navController.currentDestination?.id
    val destinationIdOfThisFragment = view?.getTag(R.id.tag_navigation_destination_id) ?: destinationIdInNavController

    return if(destinationIdInNavController == destinationIdOfThisFragment){
        view?.setTag(R.id.tag_navigation_destination_id, destinationIdOfThisFragment)
        true
    }else{
        Log.d("FragmentExtensions", "May not navigate : current destination is not the current fragment.")
        false
    }
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.hide(){
    visibility = View.GONE
}

fun MaterialButton.enableLoadingState(
    activity: MainActivity,
    @ColorInt progressBarColor: Int? = null
) {
    activity.apply {
        bindProgressButton(this@enableLoadingState)
    }

    // (Optional) Enable fade In / Fade out animations
    this.attachTextChangeAnimator()
    this.showProgress {
        progressColor = progressBarColor ?: Color.WHITE
    }
    this.isEnabled = false
}

fun MaterialButton.disableLoadingState(btnText: String) {
    this.hideProgress(btnText)
    this.isEnabled = true
}