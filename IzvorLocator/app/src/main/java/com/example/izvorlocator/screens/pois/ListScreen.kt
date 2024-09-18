package com.example.izvorlocator.screens.pois

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.izvorlocator.components.*
import com.example.izvorlocator.data.pois.Poi
import com.example.izvorlocator.ui.theme.Background
import com.example.izvorlocator.ui.theme.Secondary

@Composable
fun ListScreen(list: List<Poi>, navigateToViewPoi: () -> Unit, setSelectedPoi: (Poi) -> Unit) {
    if(list.isNotEmpty()){
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(28.dp)
        ) {
            LazyColumn {
                items(list) {
                    PoiPreview(poi = it, navigateToViewPoi, setSelectedPoi)
                }
            }
        }
    }else {
        Column(
            modifier = Modifier.padding(20.dp)
        ){
            NormalTextComponent(value = "Trenutno nema unetih izvora na mapi!")
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun PoiPreview(poi:Poi, navigateToViewPoi: () -> Unit, setSelectedPoi: (Poi) -> Unit){

    Card( modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(color = Background, shape = RoundedCornerShape(8.dp))
        .border(2.dp, color = Secondary, shape = RoundedCornerShape(8.dp))
        .clickable {
            setSelectedPoi(poi)
            navigateToViewPoi()
        },
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
            ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Text(
                            text = "Latituda:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = String.format("%.5f", poi.lat),
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Row {
                        Text(
                            text = "Longituda:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = String.format("%.5f", poi.lng),
                            fontSize = 18.sp,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            DividerTextComponent()
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "Vrsta izvora:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = poi.vrsta,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Row {
                Text(
                    text = "Kvalitet vode:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = poi.kvalitet,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Row {
                Text(
                    text = "Poslednja izmena:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = poi.createdAt.date.toString()+"."+poi.createdAt.month.toString()+
                            ". u "+ poi.createdAt.hours.toString()+":"+poi.createdAt.minutes.toString(),
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Row {
                Text(
                    text = "Kreirao korisnik:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = poi.korisnikImePrezime,
                    fontSize = 15.sp,
                )
            }
        }
    }
}