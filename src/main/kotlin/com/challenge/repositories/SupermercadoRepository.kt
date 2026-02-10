package com.challenge.repositories

import com.challenge.entities.Supermercado
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SupermercadoRepository : JpaRepository<Supermercado, Long>
