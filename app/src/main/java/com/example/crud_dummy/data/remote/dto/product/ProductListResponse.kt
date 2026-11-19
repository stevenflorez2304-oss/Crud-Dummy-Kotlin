package com.example.crud_dummy.data.remote.dto.product

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductListResponse(
    @param:Json(name = "products")
    val products: List<ProductX> = emptyList(),
    @param:Json(name = "total")
    val total: Int = 0,
    @param:Json(name = "skip")
    val skip: Int = 0,
    @param:Json(name = "limit")
    val limit: Int = 0,
)