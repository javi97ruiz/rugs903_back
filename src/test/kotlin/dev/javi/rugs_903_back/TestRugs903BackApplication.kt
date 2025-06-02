package dev.javi.rugs_903_back

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<Rugs903BackApplication>().with(TestcontainersConfiguration::class).run(*args)
}
