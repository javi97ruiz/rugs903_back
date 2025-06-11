package dev.javi.rugs_903_back.controllers

import com.cloudinary.Cloudinary
import dev.javi.rugs_903_back.dto.CustomProductRequestDto
import dev.javi.rugs_903_back.dto.CustomProductResponseDto
import dev.javi.rugs_903_back.mappers.toModel
import dev.javi.rugs_903_back.mappers.toResponseDto
import dev.javi.rugs_903_back.models.CustomProduct
import dev.javi.rugs_903_back.services.CustomProductService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/custom-products")
class CustomProductController(
    private val customProductService: CustomProductService,
    private val cloudinary: Cloudinary
) {

    @GetMapping
    fun getAll(): List<CustomProductResponseDto> {
        return customProductService.findAll().map { it.toResponseDto() }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): CustomProductResponseDto {
        return customProductService.findById(id)?.toResponseDto()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Producto personalizado no encontrado")
    }

    @PostMapping(consumes = ["multipart/form-data"])
    fun create(
        @RequestPart("image") image: MultipartFile,
        @RequestPart("name") name: String,
        @RequestPart("height") height: Int,
        @RequestPart("length") length: Int,
        @RequestPart("price") price: Double
    ): CustomProductResponseDto {
        val uploadResult = cloudinary.uploader().upload(image.bytes, emptyMap<String, Any>())
        val imageUrl = uploadResult["secure_url"] as String

        val product = CustomProduct(
            name = name,
            height = height,
            length = length,
            imageUrl = imageUrl,
            price = price
        )

        val saved = customProductService.save(product)
        return saved.toResponseDto()
    }


    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: CustomProductRequestDto): CustomProductResponseDto {
        val updated = customProductService.update(id, dto.toModel())
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Producto personalizado no encontrado")
        return updated.toResponseDto()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        customProductService.deleteById(id)
    }
}
