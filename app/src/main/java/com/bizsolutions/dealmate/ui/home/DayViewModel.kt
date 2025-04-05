package com.bizsolutions.dealmate.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bizsolutions.dealmate.db.EventEntity
import com.bizsolutions.dealmate.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(val eventRepository: EventRepository) : ViewModel() {
    fun getEventsByDate(date: LocalDate) = eventRepository.allEventsByDate(date).asLiveData()

    fun addEvent(title: String, timeStart: LocalTime, timeEnd: LocalTime, date: LocalDate, completed: Boolean = false) {
        viewModelScope.launch {
            eventRepository.insert(EventEntity(0, title, timeStart, timeEnd, date, completed))
        }
    }

    fun removeEvents(event: EventEntity) {
        viewModelScope.launch {
            eventRepository.delete(event)
        }
    }
}