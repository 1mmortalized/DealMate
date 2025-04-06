package com.bizsolutions.dealmate.repository

import com.bizsolutions.dealmate.db.DealDao
import com.bizsolutions.dealmate.db.DealEntity
import com.bizsolutions.dealmate.db.DealWithClient
import kotlinx.coroutines.flow.Flow

class DealRepository(private val dao: DealDao) {
    val allDeals: Flow<List<DealWithClient>> = dao.getAllDeals()

    suspend fun insert(deal: DealEntity) {
        dao.insertDeal(deal)
    }

    suspend fun delete(deal: DealEntity) {
        dao.deleteDeal(deal)
    }
}