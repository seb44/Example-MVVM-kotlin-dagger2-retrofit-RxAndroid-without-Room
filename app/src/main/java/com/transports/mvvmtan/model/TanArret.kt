package com.transports.mvvmtan.model



data class TanArret (

    var codeLieu: String,

    var libelle: String? = null,

    var distance: String? = null,

    var ligne: List<Ligne>? = null,

    var latitude: String? = null,

    var longitude: String? = null
)
