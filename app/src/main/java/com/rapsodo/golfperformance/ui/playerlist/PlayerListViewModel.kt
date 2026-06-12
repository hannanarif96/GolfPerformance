package com.rapsodo.golfperformance.ui.playerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rapsodo.golfperformance.domain.model.Player
import com.rapsodo.golfperformance.domain.repository.GolfRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class PlayerListViewModel(
    private val repository: GolfRepository
) : ViewModel() {

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    
    val players: StateFlow<List<Player>> = combine(_players, _searchQuery) { players, query ->
        if (query.isBlank()) {
            players
        } else {
            players.filter { 
                it.name.contains(query, ignoreCase = true) || 
                it.clubName.contains(query, ignoreCase = true) 
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        Timber.d("PlayerListViewModel initialized")
        observePlayers()
        refresh()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun observePlayers() {
        viewModelScope.launch {
            repository.getPlayers().collectLatest {
                Timber.d("Observed ${it.size} players from repository")
                _players.value = it
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Timber.d("Triggering players refresh")
                repository.refreshPlayers()
                Timber.d("Players refresh triggered successfully")
            } catch (e: Exception) {
                Timber.e(e, "Error during players refresh")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
