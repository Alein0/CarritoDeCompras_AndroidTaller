package com.example.apptienda.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.apptienda.ui.viewmodel.ProductViewModel

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CartScreen(
    viewModel: ProductViewModel,
    onPurchaseDone: () -> Unit
) {
    val cartItems by viewModel.cart.collectAsState()
    val totalPrice = cartItems.sumOf { it.price * it.quantity }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Carrito de Compras", style = MaterialTheme.typography.headlineSmall)
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(cartItems.size) { index ->
                val product = cartItems[index]
                Row (
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(product.title, style = MaterialTheme.typography.bodyLarge)
                        Text("Cantidad: ${product.quantity}")
                        Text("Precio: ${product.price}")
                    }
                    IconButton(onClick = { viewModel.removeFromCart(product) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
                Divider()
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Total: $${totalPrice}", style = MaterialTheme.typography.headlineSmall)
        Row {
            Button(onClick = {
                viewModel.clearCart()
            }) {
                Text("Vaciar carrito")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                viewModel.clearCart()
                onPurchaseDone()
            }) {
                Text("Comprar")
            }
        }
    }
}