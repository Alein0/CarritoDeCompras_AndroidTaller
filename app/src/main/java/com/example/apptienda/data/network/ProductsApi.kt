package com.example.apptienda.data.network

import com.example.apptienda.data.model.Product
import retrofit2.http.GET

interface ProductsApi {
    @GET("products")
    suspend fun getProducts(): List<Product>
}