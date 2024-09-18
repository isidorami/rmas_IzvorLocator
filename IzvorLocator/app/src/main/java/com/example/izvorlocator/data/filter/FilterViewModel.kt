package com.example.izvorlocator.data.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterViewModel : ViewModel() {

    private val _filterUIState = MutableStateFlow(FilterUIState())
    val filterUIState = _filterUIState.asStateFlow()

    fun onEvent(event: FilterUIEvent) {
        when (event) {
            is FilterUIEvent.StartDateChanged -> {
                Log.d("proba", "usli u vrste changed ++ ${event.startDate}")
                _filterUIState.value = _filterUIState.value.copy(
                    startDate = event.startDate
                )
                Log.d("proba", "nova vrednost ++ ${_filterUIState.value}")
            }

            is FilterUIEvent.EndDateChanged -> {
                Log.d("proba", "usli u vrste changed ++ ${event.endDate}")
                _filterUIState.value = _filterUIState.value.copy(
                    endDate = event.endDate
                )
                Log.d("proba", "nova vrednost ++ ${_filterUIState.value}")
            }

            is FilterUIEvent.VrsteChanged -> {
                val currentTypes = _filterUIState.value.vrste.toMutableList()
                Log.d("proba", "usli u vrste changed ++ ${event.type}")
                if (currentTypes.contains(event.type)) {
                    currentTypes.remove(event.type)
                } else {
                    currentTypes.add(event.type)
                }
                Log.d("proba", "current types ++ ${currentTypes.toString()}")
                _filterUIState.value = _filterUIState.value.copy(
                    vrste = currentTypes
                )
                Log.d("proba", "nova vrednost ++ ${_filterUIState.value}")
            }

            is FilterUIEvent.KvalitetiChanged -> {
                val currentTypes = _filterUIState.value.kvaliteti.toMutableList()
                Log.d("proba", "usli u kvaliteti changed ++ ${event.type}")
                if (currentTypes.contains(event.type)) {
                    currentTypes.remove(event.type)
                } else {
                    currentTypes.add(event.type)
                }
                Log.d("proba", "current types ++ ${currentTypes.toString()}")
                _filterUIState.value = _filterUIState.value.copy(
                    kvaliteti = currentTypes
                )
                Log.d("proba", "nova vrednost ++ ${_filterUIState.value}")
            }

            is FilterUIEvent.DistanceChanged -> {
                _filterUIState.value = _filterUIState.value.copy(
                    distance = event.distance
                )
            }

            is FilterUIEvent.UsersChanged -> {
                val currentUsers = _filterUIState.value.users.toMutableList()
                if (currentUsers.contains(event.user)) {
                    currentUsers.remove(event.user)
                } else {
                    currentUsers.add(event.user)
                }
                _filterUIState.value = _filterUIState.value.copy(
                    users = currentUsers
                )
            }

            is FilterUIEvent.SearchTextChanged -> {
                _filterUIState.value = _filterUIState.value.copy(
                    searchText = event.searchText
                )
            }

            FilterUIEvent.ResetButtonClicked -> {
                _filterUIState.value.startDate = null
                _filterUIState.value.endDate = null
                _filterUIState.value.vrste = emptyList()
                _filterUIState.value.kvaliteti = emptyList()
                _filterUIState.value.distance = null
                _filterUIState.value.users = emptyList()
                _filterUIState.value.searchText = ""
            }
        }
    }
}