package com.akushch.adlershof.domain.ports

import com.akushch.adlershof.domain.model.Area

interface FetchStationsPort {
    fun fetchStations(area: Area)
}