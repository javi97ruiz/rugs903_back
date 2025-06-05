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

        // 🔄 Borra todo (el orden importa por las FK)
        pedidoRepo.deleteAll()
        customProductRepo.deleteAll()
        productoRepo.deleteAll()
        clientRepo.deleteAll()
        userRepo.deleteAll()

        if (userRepo.count() == 0L) {
            // 👤 Usuarios
            val admin = userRepo.save(
                User(
                    id = 0,
                    username = "admin",
                    password = passwordEncoder.encode("adminpass"),
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                    isActive = true,
                    rol = "admin"
                )
            ).also { println("🟢 Usuario creado: ${it.username}") }

            val user1 = userRepo.save(
                User(
                    id = 0,
                    username = "javi1",
                    password = passwordEncoder.encode("pass1"),
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                    isActive = true,
                    rol = "user"
                )
            ).also { println("🟢 Usuario creado: ${it.username}") }

            val user2 = userRepo.save(
                User(
                    id = 0,
                    username = "javi2",
                    password = passwordEncoder.encode("pass2"),
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                    isActive = true,
                    rol = "user"
                )
            ).also { println("🟢 Usuario creado: ${it.username}") }

            val user3 = userRepo.save(
                User(
                    id = 0,
                    username = "javi3",
                    password = passwordEncoder.encode("pass3"),
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                    isActive = true,
                    rol = "user"
                )
            ).also { println("🟢 Usuario creado: ${it.username}") }

            // 👥 Clientes
            val cliente1 = clientRepo.save(
                Client(
                    id = 0,
                    address = Direccion(
                        id = 0,
                        calle = "Calle A",
                        numero = "12",
                        portal = "1",
                        piso = "2B",
                        codigoPostal = "28001",
                        ciudad = "Madrid",
                        provincia = "Madrid"
                    ),
                    phoneNumber = "600123456",
                    name = "Javi",
                    surname = "Ruiz",
                    user = user1,
                    pedidos = emptyList()
                )
            ).also { println("📘 Cliente creado: ${it.name}") }

            val cliente2 = clientRepo.save(
                Client(
                    id = 0,
                    address = Direccion(
                        id = 0,
                        calle = "Avenida B",
                        numero = "8",
                        portal = "2",
                        piso = "3A",
                        codigoPostal = "08002",
                        ciudad = "Barcelona",
                        provincia = "Barcelona"
                    ),
                    phoneNumber = "600789123",
                    name = "Ana",
                    surname = "Lopez",
                    user = user2,
                    pedidos = emptyList()
                )
            ).also { println("📘 Cliente creado: ${it.name}") }

            val cliente3 = clientRepo.save(
                Client(
                    id = 0,
                    address = Direccion(
                        id = 0,
                        calle = "Calle C",
                        numero = "3",
                        portal = "3",
                        piso = "1C",
                        codigoPostal = "46003",
                        ciudad = "Valencia",
                        provincia = "Valencia"
                    ),
                    phoneNumber = "600456789",
                    name = "Luis",
                    surname = "Garcia",
                    user = user3,
                    pedidos = emptyList()
                )
            ).also { println("📘 Cliente creado: ${it.name}") }

            // 🧶 Productos
            val prod1 = productoRepo.save(
                Product(
                    id = 0,
                    name = "Alfombra Clásica",
                    description = "Estilo tradicional",
                    price = 120.0,
                    quantity = 10
                )
            ).also { println("🧶 Producto creado: ${it.name}") }

            val prod2 = productoRepo.save(
                Product(
                    id = 0,
                    name = "Alfombra Moderna",
                    description = "Diseño actual",
                    price = 150.0,
                    quantity = 5
                )
            ).also { println("🧶 Producto creado: ${it.name}") }

            // 🧵 Productos personalizados
            customProductRepo.save(
                CustomProduct(
                    id = 0,
                    name = "Alfombra Personalizada 1",
                    height = 200,
                    length = 150,
                    imageUrl = "https://via.placeholder.com/200x150"
                )
            ).also { println("🧵 CustomProduct creado: Alfombra Personalizada 1") }

            customProductRepo.save(
                CustomProduct(
                    id = 0,
                    name = "Alfombra Personalizada 2",
                    height = 100,
                    length = 100,
                    imageUrl = "https://via.placeholder.com/100x100"
                )
            ).also { println("🧵 CustomProduct creado: Alfombra Personalizada 2") }

            // 📦 Pedidos
            pedidoRepo.save(
                Pedido(
                    id = 0,
                    clienteId = cliente1.id,
                    productId = prod1.id,
                    cantidad = 2,
                    precioUnitario = prod1.price,
                    total = prod1.price * 2,
                    fecha = "2024-06-01"
                )
            ).also { println("📦 Pedido creado para ${cliente1.name}") }

            pedidoRepo.save(
                Pedido(
                    id = 0,
                    clienteId = cliente2.id,
                    productId = prod2.id,
                    cantidad = 1,
                    precioUnitario = prod2.price,
                    total = prod2.price,
                    fecha = "2024-06-03"
                )
            ).also { println("📦 Pedido creado para ${cliente2.name}") }
        }
    }
}
