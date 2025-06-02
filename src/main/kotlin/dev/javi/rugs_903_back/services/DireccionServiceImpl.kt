package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.models.Direccion
import dev.javi.rugs_903_back.repositories.DireccionRepository
import org.springframework.stereotype.Service

@Service
class DireccionServiceImpl(
    private val direccionRepository: DireccionRepository
): DireccionService   {

    override fun getAll(): List<Direccion> {
        return direccionRepository.findAll()
    }

    override fun getById(id: Long): Direccion? {
        return direccionRepository.findById(id).orElse(null)
    }

    override fun save(direccion: Direccion): Direccion {
        return direccionRepository.save(direccion)
    }

    override fun deleteById(id: Long) {
        direccionRepository.deleteById(id)
    }
    
    override fun update(direccion: Direccion): Direccion? {
        val direccionExistente = direccionRepository.findById(direccion.id)
        return if (direccionExistente.isPresent) {
            val direccionActualizada = direccionExistente.get().copy(
                calle = direccion.calle,
                numero = direccion.numero,
                portal = direccion.portal,
                piso = direccion.piso,
                codigoPostal = direccion.codigoPostal,
                ciudad = direccion.ciudad,
                provincia = direccion.provincia
            )
            direccionRepository.save(direccionActualizada)
        } else {
            null
        }
    }

}