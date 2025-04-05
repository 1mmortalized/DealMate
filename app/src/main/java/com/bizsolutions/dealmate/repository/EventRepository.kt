package com.bizsolutions.dealmate.repository

import com.bizsolutions.dealmate.db.EventDao
import com.bizsolutions.dealmate.db.EventEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class EventRepository(private val dao: EventDao) {
    fun allEventsByDate(date: LocalDate): Flow<List<EventEntity>> = dao.getAllEventsByDate(date)

    suspend fun insert(event: EventEntity) {
        dao.insertEvent(event)
    }

    suspend fun delete(event: EventEntity) {
        dao.deleteEvent(event)
    }
}