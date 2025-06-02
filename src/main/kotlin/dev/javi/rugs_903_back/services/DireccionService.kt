package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.models.Direccion

interface DireccionService {
    fun getAll(): List<Direccion>
    fun getById(id: Long): Direccion?
    fun save(direccion: Direccion): Direccion
    fun update(direccion: Direccion): Direccion?
    fun deleteById(id: Long)

}