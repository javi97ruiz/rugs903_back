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
        clientRepo.deleteAll()
        userRepo.deleteAll()
        productoRepo.deleteAll()
        customProductRepo.deleteAll()

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

            // 👥 Clientes con direcciones (en cascada)
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

            // 🧶 Productos
            val prod1 = productoRepo.save(
                Product(
                    id = 0,
                    name = "Alfombra Clásica",
                    description = "Estilo tradicional",
                    price = 120.0,
                    quantity = 10,
                    imagen = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5BesV-aGJBJaym5RzAYDRv8LxMS48v4bvaQ&s"
                )
            )

            val prod2 = productoRepo.save(
                Product(
                    id = 0,
                    name = "Alfombra Moderna",
                    description = "Diseño actual",
                    price = 150.0,
                    quantity = 5,
                    imagen = "https://mycustomrugs.com/cdn/shop/files/Rectangular_KAWS-Inspired_Custom_Tufted_Rug_3.jpg?v=1727332429"
                )
            )

            // 🧵 Productos personalizados
            customProductRepo.save(
                CustomProduct(
                    id = 0,
                    name = "Alfombra Personalizada 1",
                    height = 200,
                    length = 150,
                    imageUrl = "https://via.placeholder.com/200x150",
                    price = 400.0
                )
            )

            customProductRepo.save(
                CustomProduct(
                    id = 0,
                    name = "Alfombra Personalizada 2",
                    height = 100,
                    length = 100,
                    imageUrl = "https://via.placeholder.com/100x100",
                    price = 200.0
                )
            )

            // 📦 Pedido 1 con líneas
            val pedido1 = Pedido(
                client = cliente1,
                fecha = "2024-06-01",
                estado = "pagado"
            )
            val linea1Pedido1 = PedidoLinea(
                pedido = pedido1,
                producto = prod1,
                cantidad = 2,
                precioUnitario = prod1.price,
                total = prod1.price * 2
            )
            pedido1.lineas += linea1Pedido1
            pedidoRepo.save(pedido1)
            println("📦 Pedido creado: $pedido1")

            // 📦 Pedido 2 con líneas
            val pedido2 = Pedido(
                client = cliente2,
                fecha = "2024-06-03",
                estado = "pagado"
            )
            val linea1Pedido2 = PedidoLinea(
                pedido = pedido2,
                producto = prod2,
                cantidad = 1,
                precioUnitario = prod2.price,
                total = prod2.price
            )
            pedido2.lineas += linea1Pedido2
            pedidoRepo.save(pedido2)
            println("📦 Pedido creado: $pedido2")
        }
    }
}
