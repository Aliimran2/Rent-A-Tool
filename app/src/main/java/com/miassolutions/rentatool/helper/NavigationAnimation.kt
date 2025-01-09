package com.miassolutions.rentatool.helper

import androidx.navigation.NavOptions
import com.miassolutions.rentatool.R

enum class NavigationAnimation {
    ANIMATION1,
    ANIMATION2,
    FADE,
    ZOOM_IN,
    ZOOM_OUT,

}

fun getNavOptions(animationType: NavigationAnimation): NavOptions {
    return when (animationType) {
        // Previous Animations
        NavigationAnimation.ANIMATION1 -> createNavOptions(
            R.anim.slide_in_up, R.anim.slide_out_down,
            R.anim.slide_in_left, R.anim.slide_out_right
        )

        NavigationAnimation.ANIMATION2 -> createNavOptions(
            R.anim.slide_in_left, R.anim.slide_out_right,
            R.anim.slide_in_up, R.anim.slide_out_down
        )

        // New Animations
        NavigationAnimation.FADE -> createNavOptions(
            R.anim.fade_in, R.anim.fade_out,
            R.anim.fade_in, R.anim.fade_out
        )

        NavigationAnimation.ZOOM_IN -> createNavOptions(
            R.anim.zoom_in, R.anim.zoom_out,
            R.anim.zoom_in, R.anim.zoom_out
        )

        NavigationAnimation.ZOOM_OUT -> createNavOptions(
            R.anim.zoom_out, R.anim.zoom_in,
            R.anim.zoom_out, R.anim.zoom_in
        )

    }
}
