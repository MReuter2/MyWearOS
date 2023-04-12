package com.example.mywearos.presentation.ui.landing

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.*
import com.example.mywearos.R
import com.example.mywearos.presentation.theme.MyWearOSTheme
import com.example.mywearos.presentation.ui.util.ReportFullyDrawn

@Composable
fun LandingScreen(
    scalingLazyListState: ScalingLazyListState,
    onClickRawSensorData: () -> Unit,
    onClickAddressBook: () -> Unit,
    modifier: Modifier = Modifier
){
    Box(modifier = modifier.fillMaxSize()){
        ScalingLazyColumn(
            state = scalingLazyListState,
            autoCentering = AutoCenteringParams(itemIndex = 0)
        ){
            item{
                ReportFullyDrawn()
                MenuChip(R.string.raw_sensordata_button_label, onClickRawSensorData)
            }

            item{
                MenuChip(R.string.address_book_button_label, onClickAddressBook)
            }
        }
    }
}

@Composable
fun MenuChip(
    @StringRes labelResourceId: Int,
    onClickAction: () -> Unit
){
    Chip(
        onClick = onClickAction,
        label = {
            Text(
                stringResource(labelResourceId),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun MenuChipPreview(){
    MyWearOSTheme {
        Column {
            MenuChip(labelResourceId = R.string.raw_sensordata_button_label) {}
            MenuChip(labelResourceId = R.string.address_book_button_label) {}
        }
    }
}