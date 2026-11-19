package com.example.crud_dummy.domain.useCase

import com.example.crud_dummy.domain.model.ProductModel
import com.example.crud_dummy.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): List<ProductModel> = repository.getProducts()
}
