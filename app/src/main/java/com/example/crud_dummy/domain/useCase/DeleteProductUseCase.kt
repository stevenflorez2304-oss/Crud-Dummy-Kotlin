package com.example.crud_dummy.domain.useCase

import com.example.crud_dummy.domain.repository.ProductRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Int): Boolean = repository.deleteProduct(id)
}
