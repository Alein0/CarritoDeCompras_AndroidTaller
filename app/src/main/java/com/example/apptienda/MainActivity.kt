package com.example.apptienda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.example.apptienda.data.local.AppDatabase
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.*
import com.example.apptienda.data.network.AuthApi
import com.example.apptienda.data.network.ProductsApi
import com.example.apptienda.repository.AuthRepository
import com.example.apptienda.repository.ProductRepository
import com.example.apptienda.ui.viewmodel.ProductViewModel
import com.example.apptienda.ui.viewmodel.AuthViewModel
import com.example.apptienda.ui.screen.LoginScreen
import com.example.apptienda.ui.screen.ProductListScreen
import com.example.apptienda.ui.screen.CartScreen
import com.example.apptienda.ui.viewmodel.AuthViewModelFactory
import com.example.apptienda.ui.viewmodel.ProductViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apptienda.data.model.Product
import androidx.compose.runtime.collectAsState



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtén el contexto antes del bloque Compose
        val context = applicationContext

        setContent {
            // Instancia Retrofit dentro de setContent
            val retrofit = Retrofit.Builder()
                .baseUrl("https://fakestoreapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            // Instancia de APIs
            val apiAuth = retrofit.create(AuthApi::class.java)
            val apiProducts = retrofit.create(ProductsApi::class.java)

            // Instancia de base de datos
            val db = AppDatabase.getInstance(context)

            // Repositorios
            val authRepository = AuthRepository(apiAuth, context)
            val productRepository = ProductRepository(apiProducts, db.productDao())

            // Factories y ViewModels
            val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository))
            val productViewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory(productRepository))

            // Navegador
            val navController = rememberNavController()

            MainNavGraph(navController, productViewModel, authViewModel)
        }
    }
}
@Composable
fun MainNavGraph(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    authViewModel: AuthViewModel
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "products" else "login"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("products") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("products") {
            ProductListScreen(
                viewModel = productViewModel,
                onProductSelected = { /* ... */ },
                onCartClick = { navController.navigate("cart") }
            )
        }
        composable("cart") {
            CartScreen(
                viewModel = productViewModel,
                onPurchaseDone = { navController.popBackStack("products", false) }
            )
        }
    }
}
// Navegación principal entre pantallas
@Composable
fun ProductListScreen(
    viewModel: ProductViewModel,
    onProductSelected: (Product) -> Unit = {},
    onCartClick: () -> Unit = {}
) {
    val products by viewModel.products.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Lista de Productos") },
            actions = {
                IconButton(onClick = { onCartClick() }) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                }
            }
        )
        LazyColumn {
            items(products.size) { index ->
                val product = products[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onProductSelected(product) }
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(product.title, style = MaterialTheme.typography.bodyLarge)
                        Text("Precio: $${product.price}")
                        Text(product.description)
                    }
                }
            }
        }
    }
}
