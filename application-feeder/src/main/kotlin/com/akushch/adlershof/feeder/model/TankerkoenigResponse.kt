package com.akushch.adlershof.feeder.model

class TankerkoenigResponse(
    val ok: Boolean,
    val license: String,
    val data: String,
    val status: String,
    val stations: List<Station>
)