package com.miassolutions.rentatool.helper

import androidx.navigation.NavOptions

fun createNavOptions(
    enterAnim: Int,
    exitAnim: Int,
    popEnterAnim: Int,
    popExitAnim: Int
): NavOptions {
    return NavOptions.Builder()
        .setEnterAnim(enterAnim)
        .setExitAnim(exitAnim)
        .setPopEnterAnim(popEnterAnim)
        .setPopExitAnim(popExitAnim)
        .build()
}
