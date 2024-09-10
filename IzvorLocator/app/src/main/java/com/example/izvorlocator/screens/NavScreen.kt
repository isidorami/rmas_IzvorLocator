package com.example.izvorlocator.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.izvorlocator.components.AppToolBar
import kotlinx.coroutines.launch
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.example.izvorlocator.R
import com.example.izvorlocator.app.AppRouter
import com.example.izvorlocator.app.Screen
import com.example.izvorlocator.data.maps.EditViewModel
import com.example.izvorlocator.data.maps.PoiViewModel
import com.example.izvorlocator.data.user.UserViewModel
import com.example.izvorlocator.screens.maps.AddPoiScreen
import com.example.izvorlocator.screens.maps.ListScreen
import com.example.izvorlocator.screens.maps.MapScreen
import com.example.izvorlocator.screens.maps.ViewPoiScreen
import com.example.izvorlocator.screens.users.ProfileScreen

@Composable
fun NavScreen(
    poiViewModel: PoiViewModel,
    userViewModel: UserViewModel = viewModel(),
    editViewModel: EditViewModel = viewModel()
) {

    val items = listOf(
        stringResource(R.string.mapa),
        stringResource(R.string.lista_izvora),
        stringResource(R.string.rangiranje_korisnika),
        stringResource(R.string.moj_profil)
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentScreen by AppRouter.currentScreen

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(220.dp)) {
                Spacer(modifier = Modifier.padding(30.dp))
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        modifier = Modifier.padding(start = 18.dp, end = 18.dp),
                        label = { Text(item) },
                        selected = when (currentScreen) {
                            is Screen.MapScreen -> index == 0
                            is Screen.ListScreen -> index == 1
                            is Screen.RangScreen -> index == 2
                            is Screen.ProfileScreen -> index == 3
                            else -> false
                        },
                        onClick = {
                            scope.launch {
                                drawerState.close()

                                when (index) {
                                    0 -> AppRouter.navigateTo(Screen.MapScreen)
                                    1 -> AppRouter.navigateTo(Screen.ListScreen)
                                    2 -> AppRouter.navigateTo(Screen.RangScreen)
                                    3 -> AppRouter.navigateTo(Screen.ProfileScreen)
                                }
                            }
                        }
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                AppToolBar(
                    toolbarTitle = when (currentScreen) {
                        is Screen.MapScreen -> items[0]
                        is Screen.ListScreen -> items[1]
                        is Screen.RangScreen -> items[2]
                        is Screen.ProfileScreen -> items[3]
                        else -> items[0]
                    },
                    burgerButtonClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    logoutButtonClicked = {
                        AppRouter.emptyStack()
                        userViewModel.logout()
                    }
                )
            }
        ) { paddingValues ->

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
            ) {

                when (currentScreen) {
                    is Screen.MapScreen -> {
                        Log.d("NavScreen", "Navigating to MapScreen")
                        val poiList = poiViewModel.pois.collectAsState(initial = listOf())

                        MapScreen(
                            onMapLongClick = { latLng ->
                                editViewModel.dodajLatLng(latLng)
                                AppRouter.navigateTo(Screen.AddScreen)
                            },
                            list = poiList.value,
                            navigateToViewPoi = {
                                AppRouter.navigateTo(Screen.ViewPoiScreen)
                            },
                            setSelectedPoi = { poiViewModel.setCurrentPoi(it) }
                        )
                    }

                    is Screen.ListScreen -> {
                        Log.d("NavScreen", "Navigating to ListScreen")
                        val poiList = poiViewModel.pois.collectAsState(initial = listOf())

                        ListScreen(
                            list = poiList.value,
                            navigateToViewPoi = {
                                AppRouter.navigateTo(Screen.ViewPoiScreen)
                            },
                            setSelectedPoi = { poiViewModel.setCurrentPoi(it) }
                        )
                    }

                    is Screen.RangScreen -> {
                        Log.d("NavScreen", "Navigating to RangScreen")
                        RangScreen()
                    }

                    is Screen.ProfileScreen -> {
                        Log.d("NavScreen", "Navigating to ProfileScreen")
                        ProfileScreen()
                    }

                    is Screen.AddScreen -> {
                        Log.d("NavScreen", "Navigating to AddScreen")
                        AddPoiScreen(
                            poiViewModel = poiViewModel,
                            navigateToMap = {
                                AppRouter.popBackStack() // Na MapScreen
                            }
                        )
                    }

                    is Screen.ViewPoiScreen -> {
                        Log.d("NavScreen", "Navigating to ViewPoiScreen")
                        ViewPoiScreen(
                            poiViewModel = poiViewModel,
                            navigateBack = {
                                AppRouter.popBackStack() // Na MapScreen
                            }
                        )
                    }

                    else -> { }
                }
            }
        }
    }
}
