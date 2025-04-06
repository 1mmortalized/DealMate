package com.bizsolutions.dealmate.repository

import com.bizsolutions.dealmate.db.CallDao
import com.bizsolutions.dealmate.db.CallEntity
import com.bizsolutions.dealmate.db.CallWithClient
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class CallRepository(private val dao: CallDao) {
    fun allCallsByDate(date: LocalDate): Flow<List<CallWithClient>> = dao.getAllCallsByDate(date)

    suspend fun insert(call: CallEntity) {
        dao.insertCall(call)
    }

    suspend fun delete(call: CallEntity) {
        dao.deleteCall(call)
    }
}