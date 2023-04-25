package com.example.mywearos.presentation.ui.addressbook

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.wear.compose.material.*
import com.example.mywearos.data.Contact
import com.example.mywearos.data.ContactRepo
import com.example.mywearos.data.sensor.Scroll
import com.example.mywearos.presentation.theme.MyWearOSTheme
import com.example.mywearos.presentation.ui.sensordata.SensorDataViewModel

@Composable
fun AddressBookScreen(sensorDataViewModel: SensorDataViewModel, modifier: Modifier = Modifier){
    val listState = rememberScalingLazyListState()
    val contactsSeparatedInLetterGroups = ContactRepo.getContactsSeparatedByFirstLetter()
    val allEvents = remember { sensorDataViewModel.allEvents }

    LaunchedEffect(
        if(allEvents.isNotEmpty()) allEvents.last() else 0
    ){
        if(allEvents.isNotEmpty() && allEvents.last() is Scroll) {
            val currentScroll = allEvents.last() as Scroll
            currentScroll.let { listState.scrollBy(it.pace.toFloat()) }
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
            item{
                Spacer(modifier = Modifier.padding(1.dp))
            }
            items(contactsSeparatedInLetterGroups.size){
                if(it != 0)
                    Spacer(modifier = Modifier.padding(10.dp))
                ContactGroup(contacts = contactsSeparatedInLetterGroups.get(it))
            }

            contactsSeparatedInLetterGroups.forEachIndexed { index, contacts ->
                item {
                    if(index != 0)
                        Spacer(modifier = Modifier.padding(10.dp))
                    ContactGroup(contacts = contacts)
                }
            }
        }
    }
}

@Composable
fun ContactGroup(contacts: List<Contact>, modifier: Modifier = Modifier){
    val sortedContacts = contacts.sortedBy { it.lastname  }
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
                for (contact in sortedContacts) {
                    ContactRow(contact = contact)
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

@Preview("Contactgroup", showBackground = true)
@Composable
private fun ContactGroupPreview(){
    MyWearOSTheme {
        ScalingLazyColumn{
            item{
                ContactGroup(contacts = ContactRepo.getContacts())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactRowPreview(){
    MyWearOSTheme {
        ContactRow(contact = Contact("Max", "Mustermann"))
    }
}