package com.miassolutions.rentatool.helper

import androidx.navigation.NavOptions
import com.miassolutions.rentatool.R

enum class NavigationAnimation {
    ANIMATION1, ANIMATION2
}

fun getNavOptions(animationType: NavigationAnimation): NavOptions {
    return when (animationType) {
        NavigationAnimation.ANIMATION1 -> createNavOptions(
            R.anim.slide_in_up, R.anim.slide_out_down,
            R.anim.slide_in_left, R.anim.slide_out_right
        )
        NavigationAnimation.ANIMATION2 -> createNavOptions(
            R.anim.slide_in_left, R.anim.slide_out_right,
            R.anim.slide_in_up, R.anim.slide_out_down
        )
    }
}
