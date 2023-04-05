package com.example.mywearos.presentation

import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.material.*
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.currentBackStackEntryAsState
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.mywearos.presentation.navigation.DestinationScrollType
import com.example.mywearos.presentation.navigation.SCROLL_TYPE_NAV_ARGUMENT
import com.example.mywearos.presentation.navigation.Screen
import com.example.mywearos.presentation.theme.MyWearOSTheme
import com.example.mywearos.presentation.theme.wearColorPalette
import com.example.mywearos.presentation.ui.ScalingLazyListStateViewModel
import com.example.mywearos.presentation.ui.ScrollStateViewModel
import com.example.mywearos.presentation.ui.landing.LandingScreen

@Composable
fun WearApp(
    modifier: Modifier = Modifier,
    swipeDissmissableNavController: NavHostController = rememberSwipeDismissableNavController()
){
    var themeColors by remember { mutableStateOf( wearColorPalette ) }
    MyWearOSTheme(
        colors = themeColors
    ) {
        val currentBackStackEntry by swipeDissmissableNavController.currentBackStackEntryAsState()
        val scrollType = currentBackStackEntry?.arguments?.getSerializable(SCROLL_TYPE_NAV_ARGUMENT) ?: DestinationScrollType.NONE

        Scaffold(
            modifier = modifier,
            vignette = {
                if(scrollType == DestinationScrollType.SCALING_LAZY_COLUMN_SCROLLING ||
                        scrollType == DestinationScrollType.COLUMN_SCROLLING
                ){
                    Vignette(vignettePosition = VignettePosition.TopAndBottom)
                }
            },
            positionIndicator = {
                when(scrollType){
                    DestinationScrollType.SCALING_LAZY_COLUMN_SCROLLING -> {
                        val scrollViewModel: ScalingLazyListStateViewModel = viewModel(currentBackStackEntry!!)
                        PositionIndicator(scalingLazyListState = scrollViewModel.scrollState)
                    }

                    DestinationScrollType.COLUMN_SCROLLING -> {
                        val viewModel: ScrollStateViewModel = viewModel(currentBackStackEntry!!)
                        PositionIndicator(scrollState = viewModel.scrollState)
                    }
                }
            }
        ){
            SwipeDismissableNavHost(
                navController = swipeDissmissableNavController,
                startDestination = Screen.Landing.route,
                modifier = Modifier.background(MaterialTheme.colors.background)
            ){
               composable(
                   route = Screen.Landing.route,
                   arguments = listOf(
                       navArgument(SCROLL_TYPE_NAV_ARGUMENT){
                           type = NavType.EnumType(DestinationScrollType::class.java)
                           defaultValue = DestinationScrollType.SCALING_LAZY_COLUMN_SCROLLING
                       }
                   )
               ){
                   val scalingLazyListState = scalingLazyListState(it)

                   LandingScreen(scalingLazyListState = scalingLazyListState)
               }
            }
        }
    }
}

@Composable
private fun scalingLazyListState(it: NavBackStackEntry): ScalingLazyListState{
    val passedScrollType = it.arguments?.getSerializable(SCROLL_TYPE_NAV_ARGUMENT)

    check(passedScrollType == DestinationScrollType.SCALING_LAZY_COLUMN_SCROLLING)

    val scrollViewModel:  ScalingLazyListStateViewModel = viewModel(it)

    return scrollViewModel.scrollState
}