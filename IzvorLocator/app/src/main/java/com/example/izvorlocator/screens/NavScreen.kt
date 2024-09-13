package com.example.izvorlocator.screens

import android.content.Context
import android.content.Intent
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
import com.example.izvorlocator.data.pois.EditViewModel
import com.example.izvorlocator.data.pois.PoiViewModel
import com.example.izvorlocator.data.user.UserViewModel
import com.example.izvorlocator.screens.pois.AddPoiScreen
import com.example.izvorlocator.screens.pois.ListScreen
import com.example.izvorlocator.screens.location.MapScreen
import com.example.izvorlocator.screens.location.SettingsScreen
import com.example.izvorlocator.screens.pois.UpdatePoiScreen
import com.example.izvorlocator.screens.pois.ViewPoiScreen
import com.example.izvorlocator.screens.users.ProfileScreen
import com.example.izvorlocator.screens.users.RangScreen

@Composable
fun NavScreen(
    poiViewModel: PoiViewModel,
    context: Context,
    startLocationService: Intent,
    stopLocationService: Intent,
    userViewModel: UserViewModel = viewModel(),
    editViewModel: EditViewModel = viewModel()
) {

    val items = listOf(
        stringResource(R.string.mapa),
        stringResource(R.string.lista_izvora),
        stringResource(R.string.rangiranje_korisnika),
        stringResource(R.string.moj_profil),
        stringResource(id = R.string.settings)
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
                            is Screen.SettingsScreen -> index == 4
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
                                    4 -> AppRouter.navigateTo(Screen.SettingsScreen)
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
                        is Screen.SettingsScreen -> items[4]
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
                        val poiList = poiViewModel.pois.collectAsState(initial = listOf())

                        MapScreen(
                            addPoi = { latLng ->
                                editViewModel.dodajLatLng(latLng)
                                AppRouter.navigateTo(Screen.AddPoiScreen)
                            },
                            list = poiList.value,
                            navigateToViewPoi = {
                                AppRouter.navigateTo(Screen.ViewPoiScreen)
                            },
                            setSelectedPoi = { poiViewModel.setCurrentPoi(it) }
                        )
                    }

                    is Screen.ListScreen -> {
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
                        RangScreen()
                    }

                    is Screen.ProfileScreen -> {
                        ProfileScreen()
                    }

                    is Screen.AddPoiScreen -> {
                        AddPoiScreen(
                            poiViewModel = poiViewModel
                        )
                    }

                    is Screen.ViewPoiScreen -> {
                        ViewPoiScreen(
                            poiViewModel = poiViewModel
                        )
                    }

                    is Screen.UpdatePoiScreen -> {
                        UpdatePoiScreen(
                            poiViewModel = poiViewModel
                        )
                    }

                    is Screen.SettingsScreen -> {
                        SettingsScreen(context, startLocationService, stopLocationService)
                    }

                    else -> { }
                }
            }
        }
    }
}
