package dev.javi.rugs_903_back.services

import com.cloudinary.Cloudinary
import dev.javi.rugs_903_back.models.CustomProduct
import dev.javi.rugs_903_back.repositories.CustomProductRepository
import org.springframework.stereotype.Service

@Service
class CustomProductServiceImpl(
    private val repository: CustomProductRepository,
    private val cloudinary: Cloudinary
) : CustomProductService {

    override fun findAll(): List<CustomProduct> = repository.findAll()

    override fun findById(id: Long): CustomProduct? =
        repository.findById(id).orElse(null)

    override fun save(product: CustomProduct): CustomProduct =
        repository.save(product)

    override fun update(id: Long, product: CustomProduct): CustomProduct? {
        val existing = repository.findById(id)
        return if (existing.isPresent) {
            val updated = existing.get().copy(
                name = product.name,
                height = product.height,
                length = product.length,
                imageUrl = product.imageUrl
            )
            repository.save(updated)
        } else null
    }

    override fun deleteById(id: Long) {
        repository.deleteById(id)
    }
}
