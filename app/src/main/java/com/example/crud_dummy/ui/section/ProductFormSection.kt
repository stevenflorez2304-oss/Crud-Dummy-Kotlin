package com.example.crud_dummy.ui.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.crud_dummy.domain.model.ProductModel

@Composable
fun ProductFormSection(
    onSaveProduct: (ProductModel) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    productToEdit: ProductModel? = null
) {
    var title by remember(productToEdit) { mutableStateOf(productToEdit?.title.orEmpty()) }
    var description by remember(productToEdit) { mutableStateOf(productToEdit?.description.orEmpty()) }
    var category by remember(productToEdit) { mutableStateOf(productToEdit?.category.orEmpty()) }
    var priceText by remember(productToEdit) { mutableStateOf(productToEdit?.price?.toString().orEmpty()) }
    var imageUrl by remember(productToEdit) { mutableStateOf(productToEdit?.imageUrl.orEmpty()) }

    var isTitleError by remember { mutableStateOf(false) }
    var isPriceError by remember { mutableStateOf(false) }

    val isEditing = productToEdit != null

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = if (isEditing) "Editar Producto" else "Agregar Nuevo Producto",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    isTitleError = it.isBlank()
                },
                label = { Text("Título *") },
                isError = isTitleError,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = priceText,
                onValueChange = {
                    priceText = it
                    isPriceError = it.toDoubleOrNull() == null
                },
                label = { Text("Precio *") },
                isError = isPriceError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL de la Imagen") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = onCancel) {
                    Text("Cancelar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        val price = priceText.toDoubleOrNull()
                        if (title.isBlank()) {
                            isTitleError = true
                            return@Button
                        }
                        if (price == null) {
                            isPriceError = true
                            return@Button
                        }

                        val product = ProductModel(
                            id = productToEdit?.id ?: 0,
                            title = title.trim(),
                            description = description.trim(),
                            category = category.trim(),
                            price = price,
                            imageUrl = imageUrl.trim()
                        )
                        onSaveProduct(product)
                    }
                ) {
                    Text(if (isEditing) "Actualizar Producto" else "Guardar Producto")
                }
            }
        }
    }
}
