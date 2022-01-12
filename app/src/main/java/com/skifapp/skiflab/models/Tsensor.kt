package com.skifapp.skiflab.models

import androidx.annotation.DrawableRes
import com.google.firebase.firestore.PropertyName
import com.skifapp.skiflab.R
import com.skifapp.skiflab.util.Constants

data class Tsensor(
    var id: String? = null,

    @get:PropertyName("id_geometry")
    @set:PropertyName("id_geometry")
    var idGeometry: String? = null,

    @get:PropertyName("id_module")
    @set:PropertyName("id_module")
    var idModule: String? = null,

    @get:PropertyName("is_notify")
    @set:PropertyName("is_notify")
    var isNotify: Boolean? = null,

    var jdata: Map<String, String>? = null,
    var name: String? = null,
    var value: Long? = null,
    var datepoint: String? = null,

    @get:PropertyName("type_tsensor")
    @set:PropertyName("type_tsensor")
    var typeTsensor: String? = null,
) {
    @get:DrawableRes
    val image: Int
        get() {
            return when (typeTsensor) {
                Constants.TSENSOR_TYPE_ELECTRICITY -> {
                    R.drawable.ic_ecectricity
                }
                Constants.TSENSOR_TYPE_GAS -> {
                    R.drawable.ic_gas
                }
                Constants.TSENSOR_TYPE_WATER -> {
                    R.drawable.ic_water
                }
                else -> {
                    R.drawable.ic_water
                }
            }
        }

}