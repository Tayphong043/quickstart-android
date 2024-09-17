package com.google.firebase.example.dataconnect.feature.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.dataconnect.movies.MoviesConnector
import com.google.firebase.dataconnect.movies.execute
import com.google.firebase.dataconnect.movies.instance
import com.google.firebase.example.dataconnect.data.toMovie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val moviesConnector: MoviesConnector = MoviesConnector.instance
) : ViewModel() {

    private val _uiState = MutableStateFlow<MoviesUIState>(MoviesUIState.Loading)
    val uiState: StateFlow<MoviesUIState>
        get() = _uiState

    init {
        viewModelScope.launch {
            try {
                val top10Movies = moviesConnector.moviesTop10.execute().data.movies.map { it.toMovie() }
                val latestMovies = moviesConnector.moviesRecentlyReleased.execute().data.movies.map { it.toMovie() }

                _uiState.value = MoviesUIState.Success(top10Movies, latestMovies)
            } catch (e: Exception) {
                _uiState.value = MoviesUIState.Error(e.localizedMessage)
            }
        }
    }
}