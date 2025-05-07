package com.bizsolutions.dealmate.repository

import com.bizsolutions.dealmate.db.ClientDao
import com.bizsolutions.dealmate.db.ClientEntity
import kotlinx.coroutines.flow.Flow

class ClientRepository(private val dao: ClientDao) {
    val allClients: Flow<List<ClientEntity>> = dao.getAllClients()

    fun getClient(id: Int) = dao.getClientById(id)

    suspend fun insert(client: ClientEntity) {
        dao.insertClient(client)
    }

    suspend fun update(client: ClientEntity) {
        dao.updateClient(client)
    }

    suspend fun delete(id: Int) {
        dao.deleteClient(id)
    }
}