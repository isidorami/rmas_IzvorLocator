package com.example.izvorlocator.screens.location

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.izvorlocator.R
import com.example.izvorlocator.components.ButtonComponent
import com.example.izvorlocator.components.DatePickerTextField
import com.example.izvorlocator.components.SizedButtonComponent
import com.example.izvorlocator.data.filter.FilterUIEvent
import com.example.izvorlocator.data.filter.FilterViewModel
import com.example.izvorlocator.data.location.LocationTracker
import com.example.izvorlocator.data.pois.Poi
import com.example.izvorlocator.data.user.UserViewModel
import com.example.izvorlocator.ui.theme.Background
import com.example.izvorlocator.ui.theme.Primary
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    addPoi: (LatLng) -> Unit,
    list: List<Poi>,
    navigateToViewPoi: () -> Unit,
    setSelectedPoi: (Poi) -> Unit,
    filterViewModel: FilterViewModel = viewModel()
) {
    // Praćenje trenutne lokacije korisnika iz LocationTracker
    val userLocation by LocationTracker.currentLocation.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(43.321445, 21.896104), 15f)
    }
    userLocation?.let {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
    }
    // Stanje filter sheet-a
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    // Korutinski scope za kontrolu filter sheet-a
    val coroutineScope = rememberCoroutineScope()

    // Filter podaci
    val state = filterViewModel.filterUIState.collectAsState()

    // Lokacije sa primenjenim filterima
    var filteredLocations by remember { mutableStateOf(list) }

    LaunchedEffect(list) {
        // Reset filteredLocations when the list changes or when entering the screen
        filteredLocations = list
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            val focusManager = LocalFocusManager.current
            // Sadržaj filter sheet-a
            Column(modifier = Modifier
                .padding(16.dp)
                .clickable(
                    onClick = { focusManager.clearFocus() },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
            ) {
                // Filter vrste
                Text("Filtriraj po vrsti izvora", color = Primary)
                LazyRow(modifier = Modifier
                    .fillMaxWidth()
                    .background(Background),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly)
                {
                    items(listOf("Česma", "Prirodni")) { type ->
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start) {
                            Checkbox(
                                checked = state.value.vrste.contains(type),
                                onCheckedChange = {
                                    filterViewModel.onEvent(FilterUIEvent.VrsteChanged(type))
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Primary,
                                    uncheckedColor = Color.Gray
                                )
                            )
                            Text(text = type, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
                // Filter kvaliteta
                Text("Filtriraj po kvalitetu vode", color = Primary)
                LazyRow(modifier = Modifier
                    .fillMaxWidth()
                    .background(Background),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly)
                {
                    items(listOf("Pijaća", "Tehnička")) { type ->
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start) {
                            Checkbox(
                                checked = state.value.kvaliteti.contains(type),
                                onCheckedChange = {
                                    filterViewModel.onEvent(FilterUIEvent.KvalitetiChanged(type))
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Primary,
                                    uncheckedColor = Color.Gray
                                )
                            )
                            Text(text = type, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
                // Filter korisnika
                Text("Filtriraj po korisnicima", color = Primary)
                LazyColumn {
                    val korisnickaImena: List<String> = list.map { it.korisnikImePrezime }.distinct()
                    items(korisnickaImena) { user ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = state.value.users.contains(user),
                                onCheckedChange = {
                                    filterViewModel.onEvent(FilterUIEvent.UsersChanged(user))
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Primary,
                                    uncheckedColor = Color.Gray
                                )
                            )
                            Text(text = user, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
                // Filter za opseg datuma
                Text("Filtriraj po opsegu datuma", color = Primary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DatePickerTextField(
                        label = "Početni datum",
                        initialDate = state.value.startDate,
                        onDateSelected = { date ->
                            filterViewModel.onEvent(FilterUIEvent.StartDateChanged(date))
                        }
                    )
                    DatePickerTextField(
                        label = "Krajnji datum",
                        initialDate = state.value.endDate,
                        onDateSelected = { date ->
                            filterViewModel.onEvent(FilterUIEvent.EndDateChanged(date))
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Filter po udaljenosti
                var distance by remember { mutableStateOf(0f) }
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text("Filtriraj po udaljenosti", color = Primary)
                    Spacer(modifier = Modifier.width(70.dp))
                    Text("Udaljenost: ${distance.toInt()} km")
                }
                Slider(
                    value = state.value.distance?:0f,
                    onValueChange = {
                        filterViewModel.onEvent(FilterUIEvent.DistanceChanged(it))},
                    valueRange = 1f..20f,
                    steps = 19,
                    colors = SliderDefaults.colors(
                        thumbColor = Primary,
                        activeTrackColor = Primary,
                        inactiveTrackColor = Color.Gray
                    )
                )

                // Filter pretraga
                Text("Pretraži po pristupačnosti", color = Primary)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    modifier = Modifier
                        .heightIn(43.dp)
                        .fillMaxWidth(),
                    value = state.value.searchText,
                    onValueChange = { filterViewModel.onEvent(FilterUIEvent.SearchTextChanged(it)) },
                    label = { Text("Unesi tekst za pretragu") },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Primary,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Primary,
                        focusedLabelColor = Primary,
                        unfocusedLabelColor = Color.Gray
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(15.dp))

                //Row(modifier = Modifier.fillMaxWidth(),
                  //  horizontalArrangement = Arrangement.SpaceBetween) {
                    /*SizedButtonComponent(
                        value = "Resetuj filtere",
                        onButtonClicked = {
                            filterViewModel.onEvent(FilterUIEvent.ResetButtonClicked)
                        },
                        width = 150.dp)*/

                    // Dugme za primenu filtera
                    ButtonComponent(
                        value = "Primeni filtere",
                        onButtonClicked = {
                            filteredLocations = list

                            userLocation?.let { _ ->
                                // Filtriranje prema vrsti
                                if (state.value.vrste.isNotEmpty()) {
                                    Log.d("proba", "state.value.vrste = ${state.value.vrste}")
                                    filteredLocations = filteredLocations.filter {
                                        it.vrsta in state.value.vrste
                                    }
                                    Log.d("proba", "filter vrste --${filteredLocations.toString()}")
                                }

                                // Filtriranje prema kvalitetu
                                if (state.value.kvaliteti.isNotEmpty()) {
                                    Log.d(
                                        "proba",
                                        "state.value.kvaliteti = ${state.value.kvaliteti}"
                                    )
                                    filteredLocations = filteredLocations.filter {
                                        it.kvalitet in state.value.kvaliteti
                                    }
                                    Log.d(
                                        "proba",
                                        "filter kvaliteta --${filteredLocations.toString()}"
                                    )
                                }

                                // Filtriranje prema korisnicima
                                if (state.value.users.isNotEmpty()) {
                                    filteredLocations = filteredLocations.filter {
                                        it.korisnikImePrezime in state.value.users
                                    }
                                    Log.d(
                                        "proba",
                                        "filter korisnika --${filteredLocations.toString()}"
                                    )
                                }

                                // Filtriranje prema opsegu datuma
                                if (state.value.startDate != null && state.value.endDate != null) {
                                    Log.d(
                                        "proba",
                                        "start=${state.value.startDate} end=${state.value.endDate}"
                                    )
                                    Log.d(
                                        "proba",
                                        "start=${Date(state.value.startDate!!)} end=${Date(state.value.endDate!!)}"
                                    )

                                    filteredLocations = filteredLocations.filter {
                                        it.createdAt >= Date(state.value.startDate!!) &&
                                                it.createdAt <= Date(state.value.endDate!!)
                                    }

                                    Log.d(
                                        "proba",
                                        "filter datuma --${filteredLocations.toString()}"
                                    )
                                }
                                // Filtriranje prema udaljenosti
                                if (state.value.distance != null) {
                                    filteredLocations = filteredLocations.filter {
                                        val startLatLng = LatLng(it.lat, it.lng)
                                        val endLatLng = LatLng(
                                            userLocation!!.latitude,
                                            userLocation!!.longitude
                                        )
                                        SphericalUtil.computeDistanceBetween(
                                            startLatLng,
                                            endLatLng
                                        ) <= state.value.distance!! * 1000
                                    }
                                    Log.d(
                                        "proba",
                                        "filter distance --${filteredLocations.toString()}"
                                    )
                                }

                                // Filtriranje prema pretrazi
                                if (state.value.searchText.isNotBlank()) {
                                    filteredLocations = filteredLocations.filter {
                                        it.doesMatchSearchQuery(state.value.searchText)
                                    }
                                }
                            }

                            // zatvori filter sheet
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.collapse()
                            }
                        },
                        isEnabled = true
                    )
                //}
            }
        },
        sheetPeekHeight = 0.dp, // filter sheet je zatvoren
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    userLocation?.let { latLng -> addPoi(latLng) }
                },
                containerColor = Primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "")
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            // Glavni sadržaj sa mapom
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
            ) {
                //Log.d("proba", "unutar gmaps locations = ${filteredLocations.toString()}")

                // Postavi markere za filtrirane lokacije
                    filteredLocations.forEach { poi ->
                        Marker(
                            state = MarkerState(position = LatLng(poi.lat, poi.lng)),
                            title = "",
                            onClick = {
                                setSelectedPoi(poi)
                                navigateToViewPoi()
                                true
                            }
                        )
                    }

                userLocation?.let {
                    Marker(
                        state = MarkerState(position = it),
                        //title = "Moja lokacija",
                        icon = getScaledBitmapDescriptor(R.drawable.ic_user_location, LocalContext.current, 48)
                    )
                }
            }

            // Dugme za otvaranje filter sheet-a
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                },
                containerColor = Primary,
                contentColor = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = "")
            }
        }
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun getScaledBitmapDescriptor(drawableResId: Int, context: Context, sizeDp: Int): BitmapDescriptor {
    val drawable: Drawable = context.getDrawable(drawableResId)!!
    val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap

    val scale = context.resources.displayMetrics.density
    val sizePx = (sizeDp * scale + 0.5f).toInt()
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, sizePx, sizePx, false)

    return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
}