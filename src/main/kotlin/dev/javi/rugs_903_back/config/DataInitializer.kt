package dev.javi.rugs_903_back.config

import dev.javi.rugs_903_back.models.*
import dev.javi.rugs_903_back.repositories.*
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

@Configuration
class DataInitializer(
    private val passwordEncoder: PasswordEncoder
) {

    @Bean
    fun initData(
        userRepo: UserRepository,
        clientRepo: ClientRepository,
        productoRepo: ProductRepository,
        customProductRepo: CustomProductRepository,
        pedidoRepo: PedidosRepository
    ) = CommandLineRunner {

        // 💣 Borra todo
        pedidoRepo.deleteAll()
        clientRepo.deleteAll()
        userRepo.deleteAll()
        productoRepo.deleteAll()
        customProductRepo.deleteAll()

        if (userRepo.count() == 0L) {
            // 👤 Usuarios
            val admin = userRepo.save(User(
                username = "admin",
                password = passwordEncoder.encode("adminpass"),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                rol = "admin"
            ))

            val user1 = userRepo.save(User("javi1", passwordEncoder.encode("pass1"), LocalDateTime.now(), LocalDateTime.now(), rol = "usuario"))
            val user2 = userRepo.save(User("javi2", passwordEncoder.encode("pass2"), LocalDateTime.now(), LocalDateTime.now(), rol = "usuario"))
            val user3 = userRepo.save(User("javi3", passwordEncoder.encode("pass3"), LocalDateTime.now(), LocalDateTime.now(), rol = "usuario"))

            // 👥 Clientes con direcciones en cascada
            val cliente1 = clientRepo.save(Client(
                address = Direccion("Calle A", "12", "1", "2B", "28001", "Madrid", "Madrid"),
                phoneNumber = "600123456",
                name = "Javi",
                surname = "Ruiz",
                user = user1
            ))

            val cliente2 = clientRepo.save(Client(
                address = Direccion("Avenida B", "8", "2", "3A", "08002", "Barcelona", "Barcelona"),
                phoneNumber = "600789123",
                name = "Ana",
                surname = "Lopez",
                user = user2
            ))

            val cliente3 = clientRepo.save(Client(
                address = Direccion("Calle C", "3", "3", "1C", "46003", "Valencia", "Valencia"),
                phoneNumber = "600456789",
                name = "Luis",
                surname = "Garcia",
                user = user3
            ))

            // 📦 Productos
            val prod1 = productoRepo.save(Product(name = "Alfombra Clásica", description = "Estilo tradicional", price = 120.0, quantity = 10))
            val prod2 = productoRepo.save(Product(name = "Alfombra Moderna", description = "Diseño actual", price = 150.0, quantity = 5))

            // 🧵 Productos personalizados
            customProductRepo.save(CustomProduct(name = "Alfombra Personalizada 1", height = 200, length = 150, imageUrl = "https://via.placeholder.com/200x150"))
            customProductRepo.save(CustomProduct(name = "Alfombra Personalizada 2", height = 100, length = 100, imageUrl = "https://via.placeholder.com/100x100"))

            // 🧾 Pedidos
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
