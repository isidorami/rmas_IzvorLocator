package com.example.izvorlocator.screens

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.izvorlocator.components.AppToolBar
import com.example.izvorlocator.data.register.RegisterViewModel
import kotlinx.coroutines.launch
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.res.stringResource
import com.example.izvorlocator.R
import com.example.izvorlocator.data.user.UserViewModel

@Composable
fun NavScreen(userViewModel: UserViewModel = viewModel(), currentScreen : String){

    val items = listOf(
        stringResource(R.string.mapa),
        stringResource(R.string.rangiranje_korisnika),
        stringResource(R.string.lista_izvora),
        stringResource(id = R.string.moj_profil))
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    var selectedTitle by rememberSaveable {
        mutableStateOf(items[0])
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(220.dp)){
                Spacer(modifier = Modifier.padding(30.dp))
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        modifier = Modifier.padding(start = 18.dp, end = 18.dp),
                        label = { Text(item) },
                        selected = index==selectedIndex,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                selectedIndex = index
                                selectedTitle = item
                            }
                        }
                    )
                }
            }
        },
        drawerState = drawerState)
    {
        Scaffold(
            topBar = {
                AppToolBar(
                    toolbarTitle = selectedTitle,
                    burgerButtonClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    logoutButtonClicked = {
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
                when(selectedTitle){
                    stringResource(R.string.mapa) -> {
                        MapScreen()
                    }
                    stringResource(R.string.rangiranje_korisnika) -> {
                        RangScreen()
                    }
                    stringResource(R.string.lista_izvora) -> {
                        ListScreen()
                    }
                    stringResource(id = R.string.moj_profil) -> {
                        ProfileScreen()
                    }
                    else -> {}
                }
            }
        }
    }
}