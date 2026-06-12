package com.rapsodo.golfperformance.ui.playerdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rapsodo.golfperformance.domain.model.Shot
import com.rapsodo.golfperformance.domain.repository.GolfRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class PlayerDetailViewModel(
    private val repository: GolfRepository
) : ViewModel() {

    private var _pagingShots: Flow<PagingData<Shot>> = emptyFlow()
    val pagingShots: Flow<PagingData<Shot>> get() = _pagingShots

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadShots(playerId: String) {
        _pagingShots = repository.getPlayerShotsPaging(playerId)
            .cachedIn(viewModelScope)
        refreshShots(playerId)
    }

    fun refreshShots(playerId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.refreshPlayerShots(playerId)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}
