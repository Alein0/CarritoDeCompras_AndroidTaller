package com.example.apptienda.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptienda.data.model.Product
import com.example.apptienda.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel (private val repository: ProductRepository): ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _cart = MutableStateFlow<List<Product>>(emptyList())
    val cart: StateFlow<List<Product>> = _cart

    fun loadProducts() {
        viewModelScope.launch {
            _products.value = repository.fetchProductsFromApi()
        }
    }

    fun loadCart() {
        viewModelScope.launch {
            _cart.value = repository.getProductsFromDb()
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            repository.addProductToCart(product)
            loadCart()
        }
    }

    fun removeFromCart(product: Product) {
        viewModelScope.launch {
            repository.removeProductFromCart(product)
            loadCart()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
            loadCart()
        }
    }
}