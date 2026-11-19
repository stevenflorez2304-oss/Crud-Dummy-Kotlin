package com.example.crud_dummy.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.crud_dummy.domain.model.ProductModel
import com.example.crud_dummy.ui.composable.DeleteConfirmationDialog
import com.example.crud_dummy.ui.composable.ErrorMessage
import com.example.crud_dummy.ui.composable.LoadingIndicator
import com.example.crud_dummy.ui.composable.ProductFormDialog
import com.example.crud_dummy.ui.section.ProductHeaderSection
import com.example.crud_dummy.ui.section.ProductListSection
import com.example.crud_dummy.ui.state.ProductUIState
import com.example.crud_dummy.ui.viewModel.ProductViewModel

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var showFormDialog by remember { mutableStateOf(false) }
    var productToEdit by remember { mutableStateOf<ProductModel?>(null) }
    var productToDelete by remember { mutableStateOf<ProductModel?>(null) }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            if (!uiState.isLoading && uiState.products.isNotEmpty()) {
                snackbarHostState.showSnackbar(message)
                viewModel.clearMessages()
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    productToEdit = null
                    showFormDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar producto",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        ProductListContent(
            uiState = uiState,
            onRetry = viewModel::loadProducts,
            onAddProductClick = {
                productToEdit = null
                showFormDialog = true
            },
            onEditProduct = { product ->
                productToEdit = product
                showFormDialog = true
            },
            onDeleteProduct = { product ->
                productToDelete = product
            },
            modifier = Modifier.padding(innerPadding)
        )

        if (showFormDialog) {
            ProductFormDialog(
                productToEdit = productToEdit,
                onDismiss = {
                    showFormDialog = false
                    productToEdit = null
                },
                onSave = { product ->
                    if (productToEdit == null) {
                        viewModel.createProduct(product)
                    } else {
                        viewModel.updateProduct(product)
                    }
                    showFormDialog = false
                    productToEdit = null
                }
            )
        }

        productToDelete?.let { product ->
            DeleteConfirmationDialog(
                product = product,
                onDismiss = { productToDelete = null },
                onConfirmDelete = {
                    viewModel.deleteProduct(product.id)
                    productToDelete = null
                }
            )
        }
    }
}

@Composable
private fun ProductListContent(
    uiState: ProductUIState,
    onRetry: () -> Unit,
    onAddProductClick: () -> Unit,
    onEditProduct: (ProductModel) -> Unit,
    onDeleteProduct: (ProductModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            uiState.isLoading -> LoadingIndicator()

            uiState.errorMessage != null && uiState.products.isEmpty() -> ErrorMessage(
                message = uiState.errorMessage,
                onRetry = onRetry
            )

            uiState.products.isEmpty() -> {
                ProductHeaderSection(
                    totalProducts = 0,
                    onAddProductClick = onAddProductClick
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No hay productos para mostrar")
                }
            }

            else -> {
                ProductHeaderSection(
                    totalProducts = uiState.products.size,
                    onAddProductClick = onAddProductClick
                )
                ProductListSection(
                    products = uiState.products,
                    onEditProduct = onEditProduct,
                    onDeleteProduct = onDeleteProduct
                )
            }
        }
    }
}
