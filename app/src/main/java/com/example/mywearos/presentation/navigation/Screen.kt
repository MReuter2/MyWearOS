package com.example.mywearos.presentation.navigation

sealed class Screen (
    val route: String
    )
{
    object Landing: Screen("landing")
    object RawSensorData: Screen("rawSensorData")
    object AddressBook: Screen("addressBook")

    object MusicPlayer: Screen("musicPlayer")
}