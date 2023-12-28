package com.defey.testapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defey.testapp.domain.MainRepository
import com.defey.testapp.domain.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        getCamera()
        getDoor()
    }

    private fun getCamera() {

        repository.getCamera().onEach { response ->
            if (response.isEmpty()) {
                syncCamera()
            }
            val groupCameras = response.groupBy { it.room }
            _uiState.update { it.copy(cameraList = groupCameras) }
        }.launchIn(viewModelScope)
    }

    private fun getDoor() {
        repository.getDoor().onEach { response ->
            if (response.isEmpty()) {
                syncDoor()
            }
            _uiState.update { it.copy(doorList = response) }
        }.launchIn(viewModelScope)

    }

    fun syncCamera() {
        viewModelScope.launch(Dispatchers.IO) {
            when (repository.synchronizeCamera()) {
                is Response.Success -> {
                    _uiState.update { it.copy(loadCamera = false) }
                }

                is Response.Failure -> {
                    _uiState.update { it.copy(loadCamera = false) }
                }

                Response.Loading -> {
                    _uiState.update { it.copy(loadCamera = true) }
                }
            }
        }
    }

    fun syncDoor() {
        viewModelScope.launch(Dispatchers.IO) {
            when (repository.synchronizeDoor()) {
                is Response.Success -> {
                    _uiState.update { it.copy(loadDoor = false) }
                }

                is Response.Failure -> {
                    _uiState.update { it.copy(loadDoor = false) }
                }

                Response.Loading -> {
                    _uiState.update { it.copy(loadDoor = true) }
                }
            }
        }
    }
}