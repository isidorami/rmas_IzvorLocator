package com.example.izvorlocator.screens.pois

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.izvorlocator.components.NormalTextComponent
import com.example.izvorlocator.components.SizedButtonComponent
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
        .border(2.dp, color = Secondary, shape = RoundedCornerShape(8.dp)),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
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
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
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
            }
            Spacer(modifier = Modifier.width(8.dp))
            SizedButtonComponent(
                value = "Više",
                onButtonClicked = {
                    setSelectedPoi(poi)
                    navigateToViewPoi()
                }, width = 150.dp
            )
        }
    }
}