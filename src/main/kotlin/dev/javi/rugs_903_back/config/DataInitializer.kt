package dev.javi.rugs_903_back.config

import dev.javi.rugs_903_back.models.*
import dev.javi.rugs_903_back.repositories.*
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

@Configuration
class DataInitializer (
    private val passwordEncoder: PasswordEncoder
){

    @Bean
    fun initData(
        userRepo: UserRepository,
        clientRepo: ClientRepository,
        direccionRepo: DireccionRepository,
        productoRepo: ProductRepository,
        customProductRepo: CustomProductRepository,
        pedidoRepo: PedidosRepository
    ) = CommandLineRunner {

        // 🔄 Fuerza reinicialización
        userRepo.deleteAll()
        direccionRepo.deleteAll()
        clientRepo.deleteAll()
        productoRepo.deleteAll()
        customProductRepo.deleteAll()
        pedidoRepo.deleteAll()

        if (userRepo.count() == 0L) {
            // Usuarios
            val admin = userRepo.save(User(
                username = "admin",
                password = passwordEncoder.encode("adminpass"),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                rol = "admin"
            ))

            val user1 = userRepo.save(User(
                username = "javi1",
                password = passwordEncoder.encode("pass1"),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                rol = "usuario"
            ))
            val user2 = userRepo.save(User(
                username = "javi2",
                password = passwordEncoder.encode("pass2"),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                rol = "usuario"
            ))
            val user3 = userRepo.save(User(
                username = "javi3",
                password = passwordEncoder.encode("pass3"),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                rol = "usuario"
            ))

            // Direcciones
            val direccion1 = direccionRepo.save(Direccion(
                calle = "Calle A", numero = "12", portal = "1", piso = "2B",
                codigoPostal = "28001", ciudad = "Madrid", provincia = "Madrid"
            ))

            val direccion2 = direccionRepo.save(Direccion(
                calle = "Avenida B", numero = "8", portal = "2", piso = "3A",
                codigoPostal = "08002", ciudad = "Barcelona", provincia = "Barcelona"
            ))

            val direccion3 = direccionRepo.save(Direccion(
                calle = "Calle C", numero = "3", portal = "3", piso = "1C",
                codigoPostal = "46003", ciudad = "Valencia", provincia = "Valencia"
            ))

            // Clientes
            val cliente1 = clientRepo.save(Client(
                id = 0,
                address = direccion1,
                phoneNumber = "600123456",
                name = "Javi",
                surname = "Ruiz",
                user = user1
            ))

            val cliente2 = clientRepo.save(Client(0, direccion2, "600789123", "Ana", "Lopez", user2))
            val cliente3 = clientRepo.save(Client(0, direccion3, "600456789", "Luis", "Garcia", user3))

            // Productos
            val prod1 = productoRepo.save(Product(name = "Alfombra Clásica", description = "Estilo tradicional", price = 120.0, quantity = 10))
            val prod2 = productoRepo.save(Product(name = "Alfombra Moderna", description = "Diseño actual", price = 150.0, quantity = 5))

            // Productos personalizados
            customProductRepo.save(CustomProduct(name = "Alfombra Personalizada 1", height = 200, length = 150, imageUrl = "https://via.placeholder.com/200x150"))
            customProductRepo.save(CustomProduct(name = "Alfombra Personalizada 2", height = 100, length = 100, imageUrl = "https://via.placeholder.com/100x100"))

            // Pedidos
            pedidoRepo.save(
                Pedido(
                    clienteId = cliente1.id,
                    productId = prod1.id,
                    cantidad = 2,
                    precioUnitario = prod1.price,
                    total = prod1.price * 2,
                    fecha = "2024-06-01"
                )
            )

            pedidoRepo.save(
                Pedido(
                    clienteId = cliente2.id,
                    productId = prod2.id,
                    cantidad = 1,
                    precioUnitario = prod2.price,
                    total = prod2.price,
                    fecha = "2024-06-03"
                )
            )
        }
    }
}
