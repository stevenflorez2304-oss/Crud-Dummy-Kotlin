package com.example.crud_dummy.ui.section


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.crud_dummy.domain.model.ProductModel
import com.example.crud_dummy.ui.composable.ProductCard

@Composable
fun ProductListSection(
    products: List<ProductModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products, key = { it.id }) { product ->
            ProductCard(product = product)
        }
    }
}