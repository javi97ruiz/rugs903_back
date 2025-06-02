package dev.javi.rugs_903_back.services

import dev.javi.rugs_903_back.models.Client
import dev.javi.rugs_903_back.repositories.ClientRepository
import org.springframework.stereotype.Service

@Service
class ClientServiceImpl(
    private val clientRepository: ClientRepository)
    : ClientService  {
    override fun getAll(): List<Client> {
        return clientRepository.findAll()
    }

    override fun getById(id: Long): Client? {
        return clientRepository.findById(id).orElse(null)
    }

    override fun save(client: Client): Client {
        return clientRepository.save(client)
    }

    override fun update(client: Client): Client? {
        return clientRepository.save(client)
    }

    override fun deleteById(id: Long) {
        clientRepository.deleteById(id)
    }

    fun getByUserId(userId: Long): Client? {
        return clientRepository.findAll().find { it.user.id == userId }
    }


}