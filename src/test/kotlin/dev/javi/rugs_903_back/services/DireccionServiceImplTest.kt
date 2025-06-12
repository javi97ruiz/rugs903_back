package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.models.Direccion
import dev.javi.rugs_903_back.repositories.DireccionRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class DireccionServiceImplTest {

    @Mock
    lateinit var direccionRepository: DireccionRepository

    @InjectMocks
    lateinit var direccionService: DireccionServiceImpl

    private fun buildDireccion(id: Long = 1L): Direccion {
        return Direccion(
            id = id,
            calle = "Calle $id",
            numero = "N$id",
            portal = "P$id",
            piso = "$id",
            codigoPostal = "2800$id",
            ciudad = "Ciudad$id",
            provincia = "Provincia$id"
        )
    }

    @Test
    fun `getAll should return list of Direccion`() {
        val direcciones = listOf(buildDireccion(1L), buildDireccion(2L))
        `when`(direccionRepository.findAll()).thenReturn(direcciones)

        val result = direccionService.getAll()

        assertEquals(direcciones, result)
    }

    @Test
    fun `getById should return Direccion when found`() {
        val direccion = buildDireccion(1L)
        `when`(direccionRepository.findById(1L)).thenReturn(Optional.of(direccion))

        val result = direccionService.getById(1L)

        assertEquals(direccion, result)
    }

    @Test
    fun `getById should return null when not found`() {
        `when`(direccionRepository.findById(1L)).thenReturn(Optional.empty())

        val result = direccionService.getById(1L)

        assertNull(result)
    }

    @Test
    fun `save should save and return Direccion`() {
        val direccion = buildDireccion(1L)
        `when`(direccionRepository.save(direccion)).thenReturn(direccion)

        val result = direccionService.save(direccion)

        assertEquals(direccion, result)
    }

    @Test
    fun `deleteById should call repository deleteById`() {
        direccionService.deleteById(1L)

        verify(direccionRepository).deleteById(1L)
    }

    @Test
    fun `update should update and return Direccion when found`() {
        val existing = buildDireccion(1L)
        val newData = buildDireccion(1L).copy(
            calle = "Nueva Calle",
            numero = "Nuevo Numero",
            portal = "Nuevo Portal",
            piso = "Nuevo Piso",
            codigoPostal = "99999",
            ciudad = "Nueva Ciudad",
            provincia = "Nueva Provincia"
        )

        `when`(direccionRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(direccionRepository.save(any())).thenAnswer { it.arguments[0] }

        val result = direccionService.update(newData)

        assertNotNull(result)
        assertEquals(newData.calle, result?.calle)
        assertEquals(newData.numero, result?.numero)
        assertEquals(newData.portal, result?.portal)
        assertEquals(newData.piso, result?.piso)
        assertEquals(newData.codigoPostal, result?.codigoPostal)
        assertEquals(newData.ciudad, result?.ciudad)
        assertEquals(newData.provincia, result?.provincia)
    }

    @Test
    fun `update should return null when Direccion not found`() {
        val newData = buildDireccion(1L)

        `when`(direccionRepository.findById(1L)).thenReturn(Optional.empty())

        val result = direccionService.update(newData)

        assertNull(result)
        verify(direccionRepository, never()).save(any())
    }
}
