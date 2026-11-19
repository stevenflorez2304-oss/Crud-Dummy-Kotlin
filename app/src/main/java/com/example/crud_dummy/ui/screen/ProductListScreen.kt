package com.example.crud_dummy.ui.screen



import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.crud_dummy.ui.composable.ErrorMessage
import com.example.crud_dummy.ui.composable.LoadingIndicator
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

    ProductListContent(
        uiState = uiState,
        onRetry = viewModel::loadProducts,
        modifier = modifier
    )
}

@Composable
private fun ProductListContent(
    uiState: ProductUIState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            uiState.isLoading -> LoadingIndicator()

            uiState.errorMessage != null -> ErrorMessage(
                message = uiState.errorMessage,
                onRetry = onRetry
            )

            uiState.products.isEmpty() -> Text(text = "No hay productos para mostrar")

            else -> {
                ProductHeaderSection(totalProducts = uiState.products.size)
                ProductListSection(products = uiState.products)
            }
        }
    }
}