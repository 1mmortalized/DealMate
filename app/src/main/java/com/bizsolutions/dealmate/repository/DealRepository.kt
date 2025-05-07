package com.bizsolutions.dealmate.repository

import com.bizsolutions.dealmate.db.DealDao
import com.bizsolutions.dealmate.db.DealEntity
import com.bizsolutions.dealmate.db.DealWithClient
import kotlinx.coroutines.flow.Flow

class DealRepository(private val dao: DealDao) {
    val allDeals: Flow<List<DealWithClient>> = dao.getAllDeals()

    fun getDeal(id: Int) = dao.getDealById(id)

    suspend fun insert(deal: DealEntity) {
        dao.insertDeal(deal)
    }

    suspend fun update(deal: DealEntity) {
        dao.updateDeal(deal)
    }

    suspend fun delete(deal: DealEntity) {
        dao.deleteDeal(deal)
    }
}