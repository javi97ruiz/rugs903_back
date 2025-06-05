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

        // ✅ Espera hasta que las tablas estén creadas
        val maxRetries = 5
        var attempts = 0
        while (attempts < maxRetries) {
            try {
                userRepo.count()
                clientRepo.count()
                productoRepo.count()
                customProductRepo.count()
                pedidoRepo.count()
                println("✅ Todas las tablas están disponibles.")
                break
            } catch (ex: Exception) {
                attempts++
                println("⏳ Esperando a que se creen las tablas... Intento $attempts/$maxRetries")
                Thread.sleep(2000)
            }
        }

        if (attempts == maxRetries) {
            println("⛔ No se pudieron verificar todas las tablas. Abortando inicialización.")
            return@CommandLineRunner
        }

        // 🔄 Borra todo en orden
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
            )
            println("🟢 Usuario creado: ${admin.username}")

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
            )
            println("🟢 Usuario creado: ${user1.username}")

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
            )
            println("🟢 Usuario creado: ${user2.username}")

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
            )
            println("🟢 Usuario creado: ${user3.username}")

            // 👥 Clientes con direcciones
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
            )
            println("📘 Cliente creado: ${cliente1.name}")

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
            )
            println("📘 Cliente creado: ${cliente2.name}")

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
            )
            println("📘 Cliente creado: ${cliente3.name}")

            // 🧶 Productos
            val prod1 = productoRepo.save(
                Product(
                    id = 0,
                    name = "Alfombra Clásica",
                    description = "Estilo tradicional",
                    price = 120.0,
                    quantity = 10
                )
            )
            println("🧶 Producto creado: ${prod1.name}")

            val prod2 = productoRepo.save(
                Product(
                    id = 0,
                    name = "Alfombra Moderna",
                    description = "Diseño actual",
                    price = 150.0,
                    quantity = 5
                )
            )
            println("🧶 Producto creado: ${prod2.name}")

            // 🧵 Productos personalizados
            val custom1 = customProductRepo.save(
                CustomProduct(
                    id = 0,
                    name = "Alfombra Personalizada 1",
                    height = 200,
                    length = 150,
                    imageUrl = "https://via.placeholder.com/200x150"
                )
            )
            println("🧵 CustomProduct creado: ${custom1.name}")

            val custom2 = customProductRepo.save(
                CustomProduct(
                    id = 0,
                    name = "Alfombra Personalizada 2",
                    height = 100,
                    length = 100,
                    imageUrl = "https://via.placeholder.com/100x100"
                )
            )
            println("🧵 CustomProduct creado: ${custom2.name}")

            // 📦 Pedidos
            val pedido1 = pedidoRepo.save(
                Pedido(
                    id = 0,
                    clienteId = cliente1.id,
                    productId = prod1.id,
                    cantidad = 2,
                    precioUnitario = prod1.price,
                    total = prod1.price * 2,
                    fecha = "2024-06-01"
                )
            )
            println("📦 Pedido creado para ${cliente1.name}")

            val pedido2 = pedidoRepo.save(
                Pedido(
                    id = 0,
                    clienteId = cliente2.id,
                    productId = prod2.id,
                    cantidad = 1,
                    precioUnitario = prod2.price,
                    total = prod2.price,
                    fecha = "2024-06-03"
                )
            )
            println("📦 Pedido creado para ${cliente2.name}")
        }
    }
}
