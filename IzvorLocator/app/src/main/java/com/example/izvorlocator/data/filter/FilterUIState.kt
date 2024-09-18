package com.example.izvorlocator.data.filter

data class FilterUIState(
    var startDate: Long? = null,
    var endDate: Long? = null,
    var vrste: List<String> = emptyList(),
    var kvaliteti: List<String> = emptyList(),
    var users: List<String> = emptyList(),
    var searchText: String = "",
    var distance: Float? = null,
)