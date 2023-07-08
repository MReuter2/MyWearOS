package com.example.mywearos.presentation.ui.contactlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.mywearos.data.Contact
import com.example.mywearos.data.ContactRepo
import com.example.mywearos.presentation.theme.MyWearOSTheme

class ContactGroupUI(private val contacts: List<Contact>) {
    private val letter = contacts[0].lastname.first()
    val height = mutableStateOf(0)
    val pixelToTop = mutableStateOf(0)

    @Composable
    fun ContactGroup(modifier: Modifier = Modifier){
        val configuration = LocalConfiguration.current
        val distanceFromCenterToTop = configuration.screenHeightDp
        pixelToTop.value = distanceFromCenterToTop
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.onGloballyPositioned {
                height.value = it.size.height
            }
        ) {
            Text(
                text = letter.toString(),
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
                    contacts.forEachIndexed { index, contact ->
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
}


@Preview("ContactGroup", showBackground = true, device = "spec:width=220dp,height=891dp")
@Composable
private fun ContactGroupPreview(){
    MyWearOSTheme {
        ContactGroupUI(ContactRepo.getContactsSeparatedByFirstLetter().first().second).ContactGroup()
    }
}