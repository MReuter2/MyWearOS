package com.example.mywearos.presentation.ui.addressbook

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.AutoCenteringParams
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberScalingLazyListState
import com.example.mywearos.data.Contact
import com.example.mywearos.data.ContactRepo
import com.example.mywearos.data.sensor.Scroll
import com.example.mywearos.data.sensor.TwoFingerScroll
import com.example.mywearos.presentation.theme.MyWearOSTheme
import com.example.mywearos.presentation.ui.sensordata.SensorDataViewModel

@Composable
fun AddressBookScreen(sensorDataViewModel: SensorDataViewModel, modifier: Modifier = Modifier){
    val listState = rememberScalingLazyListState()
    val contactsSeparatedInLetterGroups = ContactRepo.getContactsSeparatedByFirstLetter()
    val latestEvent = remember { sensorDataViewModel.latestEvent }

    LaunchedEffect(
        latestEvent
    ){
        if(latestEvent.value is Scroll) {
            val currentScroll = latestEvent.value as Scroll
            currentScroll.let { listState.scrollBy(it.pace.toFloat()) }
        }
        if(latestEvent.value is TwoFingerScroll){
            listState.animateScrollToItem(listState.centerItemIndex + 1)
        }
        /*listState.scrollToItem(
            index = if(listState.centerItemIndex + (scroll.value?.pace?.toInt() ?: 0) < 0) 0
                        else listState.centerItemIndex + (scroll.value?.pace?.toInt() ?: 0),
            scrollOffset = 0
        )*/
    }
    Box(modifier = modifier.fillMaxSize()){
        ScalingLazyColumn(
            state = listState,
            autoCentering = AutoCenteringParams(itemIndex = 0)
        ){

            contactsSeparatedInLetterGroups.forEachIndexed { index, contacts ->
                if(index == 0)
                    item{
                        Spacer(modifier = Modifier.padding(1.dp))
                    }
                item {
                    if(index != 0)
                        Spacer(modifier = Modifier.padding(10.dp))
                    ContactGroup(contacts = contacts)
                }
                if(index == contactsSeparatedInLetterGroups.lastIndex)
                    item{
                        Spacer(modifier = Modifier.padding(1.dp))
                    }
            }
        }
    }
}

@Composable
fun ContactGroup(contacts: List<Contact>, modifier: Modifier = Modifier){
    val sortedContacts = contacts.sortedBy { it.lastname  }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = sortedContacts.first().lastname[0].toString(),
            style = MaterialTheme.typography.title3,
            modifier = Modifier.padding(10.dp)
        )
        Card(
            onClick = {},
            backgroundPainter = BrushPainter(
                brush = Brush.sweepGradient(
                    listOf(MaterialTheme.colors.onPrimary, MaterialTheme.colors.onPrimary)
                )
            ),
            modifier = modifier
        ) {
            Column {
                sortedContacts.forEachIndexed { index, contact ->
                    if(index != 0)
                        Divider(
                            thickness = 1.dp,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
                            modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
                        )
                    ContactRow(contact = contact)
                }
            }
        }
    }
}

@Composable
fun ContactRow(contact: Contact, modifier: Modifier = Modifier){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(3.dp)
    ) {
        LetterIcon(letter = contact.lastname.get(0).toString())
        Text(
            "${contact.firstname} ${contact.lastname}",
            maxLines = 1,
            style = MaterialTheme.typography.caption1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier.padding(start = 5.dp)
        )
    }
}

@Composable
fun LetterIcon(letter: String, modifier: Modifier = Modifier){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.primaryVariant)
    ){
        Text(letter, style = MaterialTheme.typography.title1)
    }
}

@Preview("Contactgroup", showBackground = true, device = "spec:width=220dp,height=891dp")
@Composable
private fun ContactGroupPreview(){
    MyWearOSTheme {
        ContactGroup(contacts = ContactRepo.getContactsSeparatedByFirstLetter().first())
    }
}

@Preview(showBackground = true)
@Composable
fun ContactRowPreview(){
    MyWearOSTheme {
        ContactRow(contact = Contact("Max", "Mustermann"))
    }
}