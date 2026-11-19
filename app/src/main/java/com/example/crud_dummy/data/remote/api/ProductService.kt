package com.example.crud_dummy.data.remote.api

import com.example.crud_dummy.data.remote.dto.product.PacthProduct
import com.example.crud_dummy.data.remote.dto.product.ProductX
import com.example.crud_dummy.data.remote.dto.product.ProductListResponse
import com.example.crud_dummy.data.remote.dto.product.UpdateProductRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductService {
    @GET("products")
    suspend fun GetProduct(): ProductListResponse

    @GET( "products/{id}")
    suspend fun GetProductById(
        @Path("id") id : Int
    ) : ProductX


    @POST("products/add")
    suspend fun AddProduct(
        @Body product: UpdateProductRequest
    ): ProductX


    @PATCH("products/{id}")
    suspend fun PatchProductById(
        @Path("id") id : Int,
        @Body product: PacthProduct
    ): ProductX

    @PUT("products/{id}")
    suspend fun UpdateProductById(
        @Path("id") id: Int,
        @Body product: UpdateProductRequest
    ): ProductX


    @DELETE("products/{id}")
    suspend fun DeleteProductById(
        @Path("id") id : Int
    )


}
