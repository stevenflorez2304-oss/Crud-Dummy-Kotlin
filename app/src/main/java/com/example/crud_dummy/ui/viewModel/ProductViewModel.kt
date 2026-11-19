package com.example.crud_dummy.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crud_dummy.domain.repository.ProductRepository
import com.example.crud_dummy.ui.state.ProductUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel  @Inject constructor(
    private val repository: ProductRepository
): ViewModel(){
    private val _uiState = MutableStateFlow(ProductUIState())

    val uiState: StateFlow<ProductUIState> = _uiState.asStateFlow()

    init{ loadProducts()}

    fun loadProducts(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null)}
            runCatching {repository.getProducts()}
                .onSuccess { list -> _uiState.update { it.copy(isLoading = false, products = list)}}
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "No se pudieron cargar los productos")}}

        }
    }

}