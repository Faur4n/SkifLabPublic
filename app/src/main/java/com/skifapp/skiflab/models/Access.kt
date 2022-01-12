package com.skifapp.skiflab.models

import com.google.firebase.firestore.PropertyName
import java.util.*

data class Access(
    @get:PropertyName("acc_role")
    @set:PropertyName("acc_role")
    var accRole: String? = null,

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

    @get:PropertyName("id_module")
    @set:PropertyName("id_module")
    var idModule: String? = null,

    @get:PropertyName("id_person")
    @set:PropertyName("id_person")
    var idPerson: String? = null,

    @get:PropertyName("id_user")
    @set:PropertyName("id_user")
    var idUser: String? = null,

    var jdata: Map<String, String>? = null,

    @get:PropertyName("type_module")
    @set:PropertyName("type_module")
    var typeModule: String? = null,
    @get:PropertyName("name_module")
    @set:PropertyName("name_module")
    var nameModule : String? = null,

    var phone: String? = null
)