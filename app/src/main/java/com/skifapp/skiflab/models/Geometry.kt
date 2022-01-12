package com.skifapp.skiflab.models

import com.google.firebase.firestore.PropertyName
import java.util.*

data class Geometry(
    @get:PropertyName("dt_active")
    @set:PropertyName("dt_active")
    var dtActive: Date? = null,

    var id: String? = null,

    @get:PropertyName("id_company")
    @set:PropertyName("id_company")
    var idCompany: String? = null,

    @get:PropertyName("id_flat")
    @set:PropertyName("id_flat")
    var idFlat: String? = null,

    @get:PropertyName("id_map")
    @set:PropertyName("id_map")
    var idMap: String? = null,

    var jdata: Map<String, Any>? = null,
    var latlon: String? = null,
    var name: String? = null,

    @get:PropertyName("type_geometry")
    @set:PropertyName("type_geometry")
    var typeGeometry: String? = null,

    @get:PropertyName("type_latlon")
    @set:PropertyName("type_latlon")
    var typeLatLon: String? = null
)