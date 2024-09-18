package com.example.izvorlocator.data.filter

sealed class FilterUIEvent {
    data class StartDateChanged(val startDate: Long?) : FilterUIEvent()
    data class EndDateChanged(val endDate: Long?) : FilterUIEvent()
    data class VrsteChanged(val type: String) : FilterUIEvent()
    data class KvalitetiChanged(val type: String) : FilterUIEvent()
    data class DistanceChanged(val distance: Float?) : FilterUIEvent()
    data class UsersChanged(val user: String) : FilterUIEvent()
    data class SearchTextChanged(val searchText: String) : FilterUIEvent()

    object ResetButtonClicked : FilterUIEvent()
}