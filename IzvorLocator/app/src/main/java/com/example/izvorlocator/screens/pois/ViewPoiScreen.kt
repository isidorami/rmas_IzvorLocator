package com.example.izvorlocator.screens.pois

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.izvorlocator.R
import com.example.izvorlocator.app.AppRouter
import com.example.izvorlocator.app.Screen
import com.example.izvorlocator.components.DividerTextComponent
import com.example.izvorlocator.components.ImageSwitcher
import com.example.izvorlocator.components.SizedButtonComponent
import com.example.izvorlocator.components.UserList
import com.example.izvorlocator.data.pois.PoiViewModel
import com.example.izvorlocator.data.pois.Poi
import com.example.izvorlocator.data.user.UserViewModel
import com.example.izvorlocator.ui.theme.Background
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ViewPoiScreen(poiViewModel: PoiViewModel) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        PoiCard(poi = poiViewModel.selectedPoi,
            imageUris = poiViewModel.selectedPoiImages)
        Row {
            SizedButtonComponent(
                value = stringResource(R.string.izmeni_marker),
                onButtonClicked = {
                    AppRouter.navigateTo(Screen.UpdatePoiScreen)
                },
                width = 150.dp)
            Spacer(modifier = Modifier.width(8.dp))
            SizedButtonComponent(
                value = stringResource(R.string.obrisi_marker),
                onButtonClicked = {
                    UserViewModel.addPointsToUser(10)
                    poiViewModel.deletePoi(poiViewModel.selectedPoi.id)
                    AppRouter.popBackStack()
                },
                width = 150.dp)
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun PoiCard(poi: Poi,
            imageUris: List<Uri>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(color = Background)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(color = Background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageSwitcher(imageUris = imageUris)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row{
                    Text(
                        text = "Latituda:",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = String.format("%.5f", poi.lat),
                        fontSize = 22.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Row {
                    Text(
                        text = "Longituda:",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = String.format("%.5f", poi.lng),
                        fontSize = 22.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            DividerTextComponent()
            Spacer(modifier = Modifier.height(8.dp))
            Row{
                Text(
                    text = "Vrsta izvora:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = poi.vrsta,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row{
                Text(
                    text = "Kvalitet vode:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = poi.kvalitet,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row{
                Text(
                    text = "Pristupačnost:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = poi.pristupacnost,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "Poslednja izmena:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = poi.createdAt.date.toString()+"."+poi.createdAt.month.toString()+". u "
                            +poi.createdAt.hours.toString()+":"+poi.createdAt.minutes.toString(),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "Kreirao korisnik:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = poi.korisnikImePrezime,
                    fontSize = 18.sp,
                )
            }
        }
    }
}