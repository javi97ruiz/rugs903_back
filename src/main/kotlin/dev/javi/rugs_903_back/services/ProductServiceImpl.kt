package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.models.Product
import dev.javi.rugs_903_back.repositories.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository
) : ProductService {

    override fun getAll(): List<Product> = productRepository.findAll()

    override fun getById(id: Long): Product? = productRepository.findById(id).orElse(null)

    override fun create(product: Product): Product = productRepository.save(product)

    override fun update(id: Long, product: Product): Product? {
        return productRepository.findById(id).map {
            val updated = it.copy(
                name = product.name,
                description = product.description,
                price = product.price,
                quantity = product.quantity,
                imagen = product.imagen // ✅ Añadir esto
            )
            productRepository.save(updated)
        }.orElse(null)
    }


    override fun deleteById(id: Long) {
        productRepository.deleteById(id)
    }
}
