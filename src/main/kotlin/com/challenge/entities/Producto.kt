package com.challenge.entities

import jakarta.persistence.*

@Entity
class Producto(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var nombre: String,

    var precio: Float
)
