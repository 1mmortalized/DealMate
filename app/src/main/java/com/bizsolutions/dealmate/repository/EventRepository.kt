package com.bizsolutions.dealmate.repository

import com.bizsolutions.dealmate.db.EventDao
import com.bizsolutions.dealmate.db.EventEntity
import java.time.LocalDate

class EventRepository(private val dao: EventDao) {
    fun allEventsByDate(date: LocalDate) = dao.getAllEventsByDate(date)
    fun getEvent(id: Int) = dao.getEventById(id)

    suspend fun insert(event: EventEntity) {
        dao.insertEvent(event)
    }

    suspend fun update(event: EventEntity) {
        dao.updateEvent(event)
    }

    suspend fun delete(event: EventEntity) {
        dao.deleteEvent(event)
    }

    suspend fun completeEvent(id: Int, completed: Boolean) {
        dao.completeEvent(id, completed)
    }
}