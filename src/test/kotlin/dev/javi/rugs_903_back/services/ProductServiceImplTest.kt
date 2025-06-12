package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.models.Product
import dev.javi.rugs_903_back.repositories.ProductRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class ProductServiceImplTest {

    @Mock
    lateinit var productRepository: ProductRepository

    @InjectMocks
    lateinit var productService: ProductServiceImpl

    private fun buildProduct(id: Long = 1L, isActive: Boolean = true): Product {
        return Product(
            id = id,
            name = "Product $id",
            description = "Desc $id",
            price = 10.0,
            quantity = 5,
            imagen = "img$id",
            isActive = isActive
        )
    }

    @Test
    fun `getAll should return active products when active is true`() {
        val products = listOf(buildProduct(1L, true), buildProduct(2L, true))
        `when`(productRepository.findAllByIsActiveTrue()).thenReturn(products)

        val result = productService.getAll(true)

        assertEquals(products, result)
    }

    @Test
    fun `getAll should return inactive products when active is false`() {
        val products = listOf(buildProduct(1L, false), buildProduct(2L, false))
        `when`(productRepository.findAllByIsActiveFalse()).thenReturn(products)

        val result = productService.getAll(false)

        assertEquals(products, result)
    }

    @Test
    fun `getAll should return all products when active is null`() {
        val products = listOf(buildProduct(1L), buildProduct(2L))
        `when`(productRepository.findAll()).thenReturn(products)

        val result = productService.getAll(null)

        assertEquals(products, result)
    }

    @Test
    fun `getById should return Product when found`() {
        val product = buildProduct(1L)
        `when`(productRepository.findById(1L)).thenReturn(Optional.of(product))

        val result = productService.getById(1L)

        assertEquals(product, result)
    }

    @Test
    fun `getById should return null when not found`() {
        `when`(productRepository.findById(1L)).thenReturn(Optional.empty())

        val result = productService.getById(1L)

        assertNull(result)
    }

    @Test
    fun `create should save and return Product`() {
        val product = buildProduct(1L)
        `when`(productRepository.save(product)).thenReturn(product)

        val result = productService.create(product)

        assertEquals(product, result)
    }

    @Test
    fun `update should update and return Product when found`() {
        val existing = buildProduct(1L)
        val newData = buildProduct(1L).copy(
            name = "Updated Name",
            description = "Updated Desc",
            price = 20.0,
            quantity = 10,
            imagen = "updated-img"
        )

        `when`(productRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(productRepository.save(any())).thenAnswer { it.arguments[0] }

        val result = productService.update(1L, newData)

        assertNotNull(result)
        assertEquals(newData.name, result?.name)
        assertEquals(newData.description, result?.description)
        assertEquals(newData.price, result?.price)
        assertEquals(newData.quantity, result?.quantity)
        assertEquals(newData.imagen, result?.imagen)
    }

    @Test
    fun `update should return null when Product not found`() {
        val newData = buildProduct(1L)

        `when`(productRepository.findById(1L)).thenReturn(Optional.empty())

        val result = productService.update(1L, newData)

        assertNull(result)
        verify(productRepository, never()).save(any())
    }

    @Test
    fun `deleteById should deactivate Product when found`() {
        val product = buildProduct(1L, isActive = true)
        `when`(productRepository.findById(1L)).thenReturn(Optional.of(product))
        `when`(productRepository.save(any())).thenAnswer { it.arguments[0] }

        productService.deleteById(1L)

        assertFalse(product.isActive)
        verify(productRepository).save(product)
    }

    @Test
    fun `deleteById should throw RuntimeException when Product not found`() {
        `when`(productRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<RuntimeException> {
            productService.deleteById(1L)
        }

        verify(productRepository, never()).save(any())
    }
}
