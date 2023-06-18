package com.example.mywearos.presentation.ui.contactlist

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.itemsIndexed
import androidx.wear.compose.material.rememberScalingLazyListState
import com.example.mywearos.data.Contact
import com.example.mywearos.data.ContactRepo
import com.example.mywearos.data.Scroll
import com.example.mywearos.presentation.theme.MyWearOSTheme

@Composable
fun ContactListScreen(
    modifier: Modifier = Modifier,
    contactListViewModel: ContactListViewModel = viewModel()
){
    val contacts = contactListViewModel.contactList
    val listState = rememberScalingLazyListState(0, 0)
    val pixelsToScroll by contactListViewModel.pixelsToScroll.observeAsState(0)
    val scrollToNextItem by contactListViewModel.scrollToNextItem.observeAsState()
    ScrollAnimation(pixelsToScroll, scrollToNextItem, listState)
    ContactListScreen(
        modifier,
        contacts,
        listState
    )
}

@Composable
fun ScrollAnimation(pixelsToScroll: Int, scrollToNextItem: Scroll?, listState: ScalingLazyListState){
    LaunchedEffect(pixelsToScroll){
        listState.animateScrollBy(pixelsToScroll.toFloat())
    }
    /*LaunchedEffect(scrollToNextItem){
        var nextItem = listState.centerItemIndex
        if (scrollToNextItem != null) {
            if(scrollToNextItem.actionDirection == ActionDirection.POSITIVE)
                nextItem += 1
            else
                nextItem -= 1
        }
        if(nextItem >= 0)
            listState.scrollToItem(nextItem)
    }*/
}

@Composable
private fun ContactListScreen(
    modifier: Modifier = Modifier,
    contactsSeparatedByLetter: List<Pair<Char, List<Contact>>>,
    listState: ScalingLazyListState = rememberScalingLazyListState()
){
    Box(modifier = modifier.fillMaxSize()){
        ScalingLazyColumn(
            state = listState
        ){
            itemsIndexed(
                items = contactsSeparatedByLetter,
                key = { _, list ->
                    list.first
                }
            ){ index, contacts ->
                Spacer(modifier = Modifier.padding(if(index == 0) 1.dp else 10.dp))
                ContactGroup(contacts = contacts)
                if(index == contactsSeparatedByLetter.lastIndex)
                    Spacer(modifier = Modifier.padding(1.dp))
            }
        }
    }
}

@Composable
fun ContactGroup(contacts: Pair<Char, List<Contact>>, modifier: Modifier = Modifier){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = contacts.first.toString(),
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
                contacts.second.forEachIndexed { index, contact ->
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
        LetterIcon(letter = contact.lastname[0].toString())
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
        Text(text = letter, style = MaterialTheme.typography.title1)
    }
}

@Preview("ContactList", showBackground = true, device = "spec:width=220dp,height=891dp")
@Composable
private fun ContactListPreview(){
    val contactsSeparatedByLetter = ContactRepo.getContactsSeparatedByFirstLetter()
    MyWearOSTheme {
        ContactListScreen(contactsSeparatedByLetter = contactsSeparatedByLetter)
    }
}

@Preview("ContactGroup", showBackground = true, device = "spec:width=220dp,height=891dp")
@Composable
private fun ContactGroupPreview(){
    MyWearOSTheme {
        ContactGroup(contacts = ContactRepo.getContactsSeparatedByFirstLetter().first())
    }
}

@Preview("ContactRow", showBackground = true, device = "spec:width=220dp,height=891dp")
@Composable
private fun ContactRowPreview(){
    MyWearOSTheme {
        ContactRow(contact = Contact("Max", "Mustermann"))
    }
}

@Preview("LetterIcon", showBackground = true, device = "spec:width=220dp,height=891dp")
@Composable
private fun LetterIcon(){
    val exampleLetter = "A"
    LetterIcon(exampleLetter)
}
