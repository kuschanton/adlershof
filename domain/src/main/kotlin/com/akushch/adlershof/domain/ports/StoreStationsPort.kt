package com.akushch.adlershof.domain.ports

import com.akushch.adlershof.domain.model.Station

interface StoreStationsPort {
    fun saveStations(stations: List<Station>)
}