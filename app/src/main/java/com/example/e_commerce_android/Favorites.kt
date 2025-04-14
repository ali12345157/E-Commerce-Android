package com.example.e_commerce_android
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.e_commerce_android.ui.theme.ECommerceAndroidTheme

class Favorites:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ECommerceAndroidTheme {  }
        }
    }
}
@Composable
fun FavoritesUi(navController: NavController)
{

Scaffold(backgroundColor = Color.Gray) {  }



}