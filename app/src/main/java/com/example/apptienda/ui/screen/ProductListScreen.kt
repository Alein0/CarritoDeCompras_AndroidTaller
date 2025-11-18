package com.example.apptienda.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.example.apptienda.data.model.Product
import com.example.apptienda.ui.viewmodel.ProductViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp

@Composable
fun ProductListScreen(viewModel: ProductViewModel, onProductSelected: (Product) -> Unit) {
    val products by viewModel.products.collectAsState()

    LazyColumn {
        items(products.size) { index ->
            val product = products[index]
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { onProductSelected(product) }
                    .padding(8.dp)
            ) {
                // usar Coil para cargar imágenes: AsyncImage(model = product.image, contentDescription = product.title)
                Text(product.title, Modifier.weight(1f))
                Text("${product.price}")
            }
        }
    }
}