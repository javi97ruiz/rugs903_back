package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.models.Client

interface ClientService {
    public fun getAll(): List<Client>
    public fun getById(id: Long): Client?
    public fun save(client: Client): Client
    public fun update(client: Client): Client?
    public fun deleteById(id: Long)
}