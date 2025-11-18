package com.example.apptienda.repository

import com.example.apptienda.data.local.ProductDao
import com.example.apptienda.data.model.Product
import com.example.apptienda.data.network.ProductsApi

class ProductRepository (
    private val api: ProductsApi,
    private val dao: ProductDao
) {
    suspend fun fetchProductsFromApi(): List<Product> = api.getProducts()
    suspend fun getProductsFromDb(): List<Product> = dao.getAll()
    suspend fun addProductToCart(product: Product) = dao.insert(product)
    suspend fun removeProductFromCart(product: Product) = dao.delete(product)
    suspend fun clearCart() = dao.deleteAll()
}