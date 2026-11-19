package com.example.crud_dummy.domain.repository

import com.example.crud_dummy.domain.model.ProductModel

interface ProductRepository {
    suspend fun getProducts(): List<ProductModel>
    suspend fun getProductById(id: Int): ProductModel
    suspend fun createProduct(product: ProductModel): ProductModel
    suspend fun updateProduct( product: ProductModel): ProductModel
    suspend fun deleteProduct(id: Int) : Boolean

}