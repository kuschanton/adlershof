package com.akushch.adlershof.feeder.model

import java.util.UUID

data class Station (
    val id : UUID,
    val name : String,
    val brand : String,
    val street : String,
    val place : String,
    val lat : Double,
    val lng : Double,
    val dist : Double,
    val diesel : Double,
    val e5 : Double,
    val e10 : Double,
    val isOpen : Boolean,
    val houseNumber : String,
    val postCode : Int
)