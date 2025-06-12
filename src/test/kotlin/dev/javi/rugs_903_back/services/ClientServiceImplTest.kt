package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.models.Client
import dev.javi.rugs_903_back.models.Direccion
import dev.javi.rugs_903_back.models.User
import dev.javi.rugs_903_back.repositories.ClientRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class ClientServiceImplTest {

    @Mock
    lateinit var clientRepository: ClientRepository

    @InjectMocks
    lateinit var clientService: ClientServiceImpl

    fun buildClient(id: Long = 1L, isActive: Boolean = true): Client {
        return Client(
            id = id,
            phoneNumber = "612345678",
            name = "John",
            surname = "Doe",
            user = User(
                id = 1L,
                username = "johndoe",
                email = "john@example.com",
                password = "password",
                rol = "USER",
                isActive = true,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            address = Direccion(
                id = 1L,
                calle = "Calle Falsa",
                numero = "123",
                portal = "A",
                piso = "1",
                codigoPostal = "28080",
                ciudad = "Madrid",
                provincia = "Madrid"
            ),
            isActive = isActive
        )
    }

    @Test
    fun `getAll should return active clients when active is true`() {
        val clients = listOf(buildClient(1L, true), buildClient(2L, true))
        `when`(clientRepository.findAllByIsActiveTrue()).thenReturn(clients)

        val result = clientService.getAll(true)

        assertEquals(clients, result)
    }

    @Test
    fun `getAll should return inactive clients when active is false`() {
        val clients = listOf(buildClient(1L, false), buildClient(2L, false))
        `when`(clientRepository.findAllByIsActiveFalse()).thenReturn(clients)

        val result = clientService.getAll(false)

        assertEquals(clients, result)
    }

    @Test
    fun `getAll should return all clients when active is null`() {
        val clients = listOf(buildClient(1L, true), buildClient(2L, false))
        `when`(clientRepository.findAll()).thenReturn(clients)

        val result = clientService.getAll(null)

        assertEquals(clients, result)
    }

    @Test
    fun `getById should return client when found`() {
        val client = buildClient(1L)
        `when`(clientRepository.findById(1L)).thenReturn(Optional.of(client))

        val result = clientService.getById(1L)

        assertEquals(client, result)
    }

    @Test
    fun `getById should return null when not found`() {
        `when`(clientRepository.findById(1L)).thenReturn(Optional.empty())

        val result = clientService.getById(1L)

        assertNull(result)
    }

    @Test
    fun `save should save and return client`() {
        val client = buildClient(1L)
        `when`(clientRepository.save(client)).thenReturn(client)

        val result = clientService.save(client)

        assertEquals(client, result)
    }

    @Test
    fun `update should update and return client`() {
        val client = buildClient(1L)
        `when`(clientRepository.save(client)).thenReturn(client)

        val result = clientService.update(client)

        assertEquals(client, result)
    }

    @Test
    fun `deleteById should deactivate client when found`() {
        val client = buildClient(1L, isActive = true)
        `when`(clientRepository.findById(1L)).thenReturn(Optional.of(client))
        `when`(clientRepository.save(any())).thenReturn(client)

        clientService.deleteById(1L)

        // Verificar que el isActive del cliente se haya puesto a false
        assertFalse(client.isActive)

        // Verificar que se haya llamado a save
        verify(clientRepository).save(client)
    }

    @Test
    fun `deleteById should throw RuntimeException when client not found`() {
        `when`(clientRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<RuntimeException> {
            clientService.deleteById(1L)
        }

        verify(clientRepository, never()).save(any())
    }

    @Test
    fun `getByUsername should return client when found`() {
        val username = "johndoe"
        val client = buildClient(1L)
        `when`(clientRepository.findByUserUsername(username)).thenReturn(client)

        val result = clientService.getByUsername(username)

        assertEquals(client, result)
    }
}
