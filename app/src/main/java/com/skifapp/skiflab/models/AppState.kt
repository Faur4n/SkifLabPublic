package com.skifapp.skiflab.models

data class AppState(
    val accesses: List<Access> = emptyList(),
    val currentAccess: Access? = null,
    val sensors: List<Tsensor?> = arrayOfNulls<Tsensor>(5).toList()
)