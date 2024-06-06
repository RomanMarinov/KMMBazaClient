package domain.repository

import domain.model.history_call.HistoryCallAddress

interface HistoryCallRepository {
    suspend fun getHistoryCall() : List<HistoryCallAddress>?
}