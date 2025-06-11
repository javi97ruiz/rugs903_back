package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.models.Client

interface ClientService {
    fun getAll(active: Boolean?): List<Client>
    public fun getById(id: Long): Client?
    public fun save(client: Client): Client
    public fun update(client: Client): Client?
    public fun deleteById(id: Long)
    fun getByUsername(username: String): Client?

}