package com.example.mywearos.presentation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.wear.compose.material.*
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.mywearos.presentation.navigation.Screen
import com.example.mywearos.presentation.theme.MyWearOSTheme
import com.example.mywearos.presentation.ui.contactlist.ContactListScreen
import com.example.mywearos.presentation.ui.landing.LandingScreen
import com.example.mywearos.presentation.ui.musicplayer.SongScreen
import com.example.mywearos.presentation.ui.sensordata.RawSensorDataScreen

@Composable
fun WearApp(
    modifier: Modifier = Modifier,
    swipeDismissibleNavController: NavHostController = rememberSwipeDismissableNavController()
){
    MyWearOSTheme {
        Scaffold(
            modifier = modifier,
            vignette = {
                    Vignette(vignettePosition = VignettePosition.TopAndBottom)
            }
        ){
            SwipeDismissableNavHost(
                navController = swipeDismissibleNavController,
                startDestination = Screen.Landing.route,
                modifier = Modifier.background(MaterialTheme.colors.background)
            ){
                composable(
                   route = Screen.Landing.route
                ){
                   LandingScreen(
                       onClickRawSensorData = { swipeDismissibleNavController.navigate(Screen.RawSensorData.route) },
                       onClickAddressBook = { swipeDismissibleNavController.navigate(Screen.AddressBook.route) },
                       onClickMusicPlayer = { swipeDismissibleNavController.navigate(Screen.MusicPlayer.route) }
                   )
                }

                composable(
                    route = Screen.RawSensorData.route
                ){
                    RawSensorDataScreen()
                }

                composable(
                    route = Screen.AddressBook.route
                ){
                    ContactListScreen()
                }

                composable(
                    route = Screen.MusicPlayer.route
                ){
                    SongScreen()
                }
            }
        }
    }
}