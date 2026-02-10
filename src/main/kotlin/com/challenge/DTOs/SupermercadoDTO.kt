package com.challenge.DTOs

import java.time.DayOfWeek
import java.time.LocalTime

data class SupermercadoDTO(
    val id: Long?=null,

    var nombre: String?=null,

    var horarioApertura: LocalTime?=null,
    var horarioCierre: LocalTime?=null,

    val diasAbierto: MutableSet<DayOfWeek>?=null)
