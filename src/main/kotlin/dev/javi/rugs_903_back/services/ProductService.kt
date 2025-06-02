package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.models.Product

interface ProductService {
    fun getAll(): List<Product>
    fun getById(id: Long): Product?
    fun create(product: Product): Product
    fun update(id: Long, product: Product): Product?
    fun deleteById(id: Long)
}
