package com.example.crud_dummy.data.remote.dto.product

import com.squareup.moshi.Json

data class PacthProduct(
    @param:Json(name = "category")
    val category: String,
)
