package com.akushch.adlershof.domain.usecase

import com.akushch.adlershof.domain.model.Area

interface UpdateStationsInAreaUseCase {
    fun updateStationsInArea(area: Area)
}