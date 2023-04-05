package com.example.mywearos.presentation.navigation

const val SCROLL_TYPE_NAV_ARGUMENT = "scrollType"

sealed class Screen (
    val route: String
    )
{
 object Landing: Screen("landing")
}