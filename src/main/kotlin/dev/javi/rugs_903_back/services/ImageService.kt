package dev.javi.rugs_903_back.services

import com.cloudinary.Cloudinary
import org.springframework.boot.context.properties.bind.Bindable.mapOf
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import kotlin.collections.mapOf

@Service
class ImageService(
    private val cloudinary: Cloudinary
) {

    fun uploadImage(file: MultipartFile): String {
        val uploadResult = cloudinary.uploader().upload(file.bytes, mapOf<String, Any>())

        return uploadResult["secure_url"] as String
    }
}
