package com.bizsolutions.dealmate.repository

import com.bizsolutions.dealmate.db.CallDao
import com.bizsolutions.dealmate.db.CallEntity
import com.bizsolutions.dealmate.db.CallWithClient
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class CallRepository(private val dao: CallDao) {
    fun allCallsByDate(date: LocalDate): Flow<List<CallWithClient>> = dao.getAllCallsByDate(date)
    fun getCall(id: Int) = dao.getCallById(id)

    suspend fun insert(call: CallEntity) {
        dao.insertCall(call)
    }

    suspend fun update(call: CallEntity) {
        dao.updateCall(call)
    }

    suspend fun delete(call: CallEntity) {
        dao.deleteCall(call)
    }

    suspend fun completeCall(id: Int, completed: Boolean) {
        dao.completeCall(id, completed)
    }
}