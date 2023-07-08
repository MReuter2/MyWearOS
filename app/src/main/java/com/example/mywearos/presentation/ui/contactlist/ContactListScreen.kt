package com.example.mywearos.presentation.ui.contactlist

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListAnchorType
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.itemsIndexed
import androidx.wear.compose.material.rememberScalingLazyListState
import com.example.mywearos.data.Contact
import com.example.mywearos.data.ContactRepo
import com.example.mywearos.data.Scroll
import com.example.mywearos.presentation.theme.MyWearOSTheme
import kotlinx.coroutines.launch

@Composable
fun ContactListScreen(
    contactListViewModel: ContactListViewModel = viewModel()
){
    val contacts = contactListViewModel.contactList
    val listState = rememberScalingLazyListState(0, 0)
    val contactGroupUis = remember{ mutableStateListOf<ContactGroupUI>() }

    val pixelsToScroll by contactListViewModel.pixelsToScroll.observeAsState(0)
    val doubleFingerScroll by contactListViewModel.doubleFingerScroll.observeAsState(Scroll(0, 0))

    GroupStepAnimation(
        listState = listState,
        contactGroupUIs = contactGroupUis,
        doubleFingerScroll = doubleFingerScroll
    )
    ScrollAnimation(
        pixelsToScroll = pixelsToScroll,
        listState = listState
    )
    ContactListScreen(
        contactsSeparatedByLetter = contacts,
        listState = listState,
        contactGroupUis = contactGroupUis
    )
}

@Composable
fun GroupStepAnimation(
    listState: ScalingLazyListState = rememberScalingLazyListState(),
    contactGroupUIs: List<ContactGroupUI>,
    doubleFingerScroll: Scroll
){
    LaunchedEffect(doubleFingerScroll){
        val currentGroup = contactGroupUIs[listState.centerItemIndex]
        val previousGroup = if(listState.centerItemIndex != 0)
            contactGroupUIs[listState.centerItemIndex - 1] else currentGroup
        val distanceFromCenterToTop = currentGroup.pixelToTop.value
        val pixelToCurrentGroupTop = (- listState.centerItemScrollOffset) + distanceFromCenterToTop
        val pixelToPreviousGroup = (- listState.centerItemScrollOffset - previousGroup.height.value) + distanceFromCenterToTop
        val pixelToNextGroup = currentGroup.height.value - listState.centerItemScrollOffset + distanceFromCenterToTop
        if(doubleFingerScroll.pace > 0){
            if(listState.centerItemIndex != contactGroupUIs.lastIndex){
                listState.scrollBy(pixelToNextGroup.toFloat())
            }
        }else if(doubleFingerScroll.pace < 0){
            if(pixelToCurrentGroupTop != 0){
                listState.scrollBy(pixelToCurrentGroupTop.toFloat())
            }else{
                listState.scrollBy(pixelToPreviousGroup.toFloat())
            }
        }
    }
}

@Composable
fun ScrollAnimation(pixelsToScroll: Int, listState: ScalingLazyListState = rememberScalingLazyListState()){
    LaunchedEffect(pixelsToScroll){
        listState.animateScrollBy(pixelsToScroll.toFloat())
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ContactListScreen(
    modifier: Modifier = Modifier,
    contactsSeparatedByLetter: List<Pair<Char, List<Contact>>>,
    listState: ScalingLazyListState = rememberScalingLazyListState(),
    contactGroupUis: MutableList<ContactGroupUI> = mutableListOf()
){
    val focusRequester = remember{ FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()){
        ScalingLazyColumn(
            state = listState,
            anchorType = ScalingLazyListAnchorType.ItemStart,
            modifier = modifier
                .onRotaryScrollEvent {
                    coroutineScope.launch {
                        listState.scrollBy(it.verticalScrollPixels)
                    }
                    true
                }
                .focusRequester(focusRequester)
                .focusable()
        ){
            itemsIndexed(
                items = contactsSeparatedByLetter
            ){ index, contacts ->
                Spacer(modifier = Modifier.padding(if(index == 0) 1.dp else 10.dp))
                Box(
                    modifier = Modifier
                ){
                    val contactGroupUI = ContactGroupUI(contacts.second)
                    contactGroupUis.add(index, contactGroupUI)
                    contactGroupUI.ContactGroup()
                }
                if(index == contactsSeparatedByLetter.lastIndex)
                    Spacer(modifier = Modifier.padding(1.dp))
            }
        }
        LaunchedEffect(Unit){
            focusRequester.requestFocus()
        }
    }
}

/*@Composable
fun ContactGroup(contacts: Pair<Char, List<Contact>>, modifier: Modifier = Modifier){
    val height = remember { mutableStateOf(0) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.onGloballyPositioned {
            height.value = it.size.height
        }
    ) {
        Text(
            text = contacts.first.toString() + ", H: ${height.value}",
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
}*/

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
