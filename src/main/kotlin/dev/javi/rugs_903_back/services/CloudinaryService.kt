package dev.javi.rugs_903_back.services

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class CloudinaryService(
    private val cloudinary: Cloudinary
) {

    fun uploadImage(file: MultipartFile, folder: String? = null): String {
        val options = mutableMapOf<String, Any>()
        folder?.let { options["folder"] = it }

        val uploadResult = cloudinary.uploader().upload(file.bytes, options)
        return uploadResult["secure_url"] as String
    }

    fun deleteImage(publicId: String) {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap())
    }
}
