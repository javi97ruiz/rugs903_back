package dev.javi.rugs_903_back

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Component

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
class Rugs903BackApplication

fun main(args: Array<String>) {
    runApplication<Rugs903BackApplication>(*args)
}


@Component
class AppStartupListener : ApplicationListener<ApplicationReadyEvent> {
    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        println("✅ La aplicación ha arrancado correctamente en http://localhost:8080")
    }
}