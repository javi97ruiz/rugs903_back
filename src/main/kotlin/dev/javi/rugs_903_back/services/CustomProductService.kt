package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.models.CustomProduct

interface CustomProductService {
    fun findAll(): List<CustomProduct>
    fun findById(id: Long): CustomProduct?
    fun save(product: CustomProduct): CustomProduct
    fun update(id: Long, product: CustomProduct): CustomProduct?
    fun deleteById(id: Long)
}
