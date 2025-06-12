package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.models.CustomProduct
import dev.javi.rugs_903_back.repositories.CustomProductRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class CustomProductServiceImplTest {

    @Mock
    lateinit var repository: CustomProductRepository

    @InjectMocks
    lateinit var service: CustomProductServiceImpl

    private fun buildCustomProduct(id: Long = 1L): CustomProduct {
        return CustomProduct(
            id = id,
            name = "Custom Product $id",
            height = 100,
            length = 200,
            imageUrl = "https://example.com/product$id.jpg",
            price = 99.99,
            pedido = null
        )
    }

    @Test
    fun `findAll should return list of CustomProduct`() {
        val products = listOf(buildCustomProduct(1L), buildCustomProduct(2L))
        `when`(repository.findAll()).thenReturn(products)

        val result = service.findAll()

        assertEquals(products, result)
    }

    @Test
    fun `findById should return CustomProduct when found`() {
        val product = buildCustomProduct(1L)
        `when`(repository.findById(1L)).thenReturn(Optional.of(product))

        val result = service.findById(1L)

        assertEquals(product, result)
    }

    @Test
    fun `findById should return null when not found`() {
        `when`(repository.findById(1L)).thenReturn(Optional.empty())

        val result = service.findById(1L)

        assertNull(result)
    }

    @Test
    fun `save should save and return CustomProduct`() {
        val product = buildCustomProduct(1L)
        `when`(repository.save(product)).thenReturn(product)

        val result = service.save(product)

        assertEquals(product, result)
    }

    @Test
    fun `update should update and return CustomProduct when found`() {
        val existing = buildCustomProduct(1L)
        val newData = buildCustomProduct(1L).copy(
            name = "Updated Product",
            height = 150,
            length = 250,
            imageUrl = "https://example.com/updated.jpg"
        )

        `when`(repository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(repository.save(any())).thenAnswer { it.arguments[0] }

        val result = service.update(1L, newData)

        assertNotNull(result)
        assertEquals(newData.name, result?.name)
        assertEquals(newData.height, result?.height)
        assertEquals(newData.length, result?.length)
        assertEquals(newData.imageUrl, result?.imageUrl)
    }

    @Test
    fun `update should return null when CustomProduct not found`() {
        val newData = buildCustomProduct(1L)

        `when`(repository.findById(1L)).thenReturn(Optional.empty())

        val result = service.update(1L, newData)

        assertNull(result)
        verify(repository, never()).save(any())
    }

    @Test
    fun `deleteById should call repository deleteById`() {
        service.deleteById(1L)

        verify(repository).deleteById(1L)
    }
}
