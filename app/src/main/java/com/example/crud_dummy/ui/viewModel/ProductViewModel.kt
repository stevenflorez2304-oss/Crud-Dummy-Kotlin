package com.example.crud_dummy.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crud_dummy.domain.model.ProductModel
import com.example.crud_dummy.domain.useCase.CreateProductUseCase
import com.example.crud_dummy.domain.useCase.DeleteProductUseCase
import com.example.crud_dummy.domain.useCase.GetProductByIdUseCase
import com.example.crud_dummy.domain.useCase.GetProductsUseCase
import com.example.crud_dummy.domain.useCase.UpdateProductUseCase
import com.example.crud_dummy.ui.state.ProductUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val createProductUseCase: CreateProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUIState())
    val uiState: StateFlow<ProductUIState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { getProductsUseCase() }
                .onSuccess { list ->
                    _uiState.update { it.copy(isLoading = false, products = list) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "No se pudieron cargar los productos"
                        )
                    }
                }
        }
    }

    fun getProductById(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { getProductByIdUseCase(id) }
                .onSuccess { product ->
                    _uiState.update { it.copy(isLoading = false, selectedProduct = product) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "No se pudo obtener el producto"
                        )
                    }
                }
        }
    }

    fun createProduct(product: ProductModel) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, successMessage = null) }
            runCatching { createProductUseCase(product) }
                .onSuccess { newProduct ->
                    _uiState.update { state ->
                        state.copy(
                            isSaving = false,
                            products = state.products + newProduct,
                            successMessage = "Producto creado exitosamente"
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = e.message ?: "No se pudo crear el producto"
                        )
                    }
                }
        }
    }

    fun updateProduct(product: ProductModel) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, successMessage = null) }
            runCatching { updateProductUseCase(product) }
                .onSuccess { updatedProduct ->
                    _uiState.update { state ->
                        state.copy(
                            isSaving = false,
                            products = state.products.map { if (it.id == updatedProduct.id) updatedProduct else it },
                            selectedProduct = updatedProduct,
                            successMessage = "Producto actualizado exitosamente"
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = e.message ?: "No se pudo actualizar el producto"
                        )
                    }
                }
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, successMessage = null) }
            runCatching { deleteProductUseCase(id) }
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            isSaving = false,
                            products = state.products.filter { it.id != id },
                            selectedProduct = if (state.selectedProduct?.id == id) null else state.selectedProduct,
                            successMessage = "Producto eliminado exitosamente"
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = e.message ?: "No se pudo eliminar el producto"
                        )
                    }
                }
        }
    }

    fun selectProduct(product: ProductModel?) {
        _uiState.update { it.copy(selectedProduct = product) }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
