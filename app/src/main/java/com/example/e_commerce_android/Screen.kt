package com.example.e_commerce_android

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Categories : Screen("categories")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
}
