package com.example.e_commerce_android
import HomeScreen
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.e_commerce_android.ui.theme.ECommerceAndroidTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ECommerceAndroidTheme {
                AppNavigation()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiSignIn(navController: NavController) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("") }
    val loading = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    BoxWithConstraints {
        val maxWidth = maxWidth
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.main))
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.signin),
                contentDescription = "Sign In Image",
                modifier = Modifier.size(if (maxWidth > 600.dp) 200.dp else 150.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Welcome Back To Route",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontSize = if (maxWidth > 600.dp) 24.sp else 18.sp
            )
            Text(
                text = "Please sign in with your mail",
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontSize = if (maxWidth > 600.dp) 20.sp else 14.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "E-Mail",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                placeholder = { Text("Enter your email") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Black,
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )
            Text(
                text = "Password",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Enter your password") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Black,
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = if (passwordVisible) R.drawable.openeye else R.drawable.eye),
                            contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                            tint = Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
            Text(
                text = "Forgot Password",
                color = Color.White,
                modifier = Modifier.fillMaxWidth().clickable { navController.navigate("forgetPassword") },
                textAlign = TextAlign.End
            )
            Button(
                onClick = {
                    loading.value = true
                    val request = SignInRequest(userName, password)
                    ApiManager.authService.signIn(request).enqueue(object : Callback<SignInResponse> {
                        override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
                            loading.value = false
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Sign in successful!", Toast.LENGTH_SHORT).show()
                                navController.navigate("home")
                            } else {
                                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                            loading.value = false
                            Toast.makeText(context, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Login", textAlign = TextAlign.Center, color = Color.Black)
            }
            Text(
                text = "Don’t have an account? Create Account",
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.clickable { navController.navigate("signup") }
            )
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val screensWithBottomBar = listOf("home", "categories", "favorites", "profile")
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in screensWithBottomBar) {
                NavigationBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(navController = navController, startDestination = "login", modifier = Modifier.padding(paddingValues)) {
            composable("login") { UiSignIn(navController) }
            composable("signup") { UiSignUp(navController) }
            composable("forgetPassword") { ForgetPasswordScreen(navController) }
            composable("verify") { VerifyPassword(navController) }
            composable("New") { NewPassword(navController) }
            composable("home") { HomeScreen(navController) }
            composable("categories") { CategoriesUi(navController) }
            composable("favorites") { FavoritesUi(navController) }
            composable("profile") { ProfileUi(navController) }

        }
    }
}

@Composable
fun NavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("home", R.drawable.home, 32),
        BottomNavItem("categories", R.drawable.category, 28),
        BottomNavItem("favorites", R.drawable.love, 28),
        BottomNavItem("profile", R.drawable.user, 28)
    )

    var selectedItem by remember { mutableStateOf(0) }

    BottomNavigation(
        backgroundColor = Color(0xFF004182),
        contentColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                icon = {
                    androidx.compose.material.Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(item.iconSize.dp),
                        tint = if (selectedItem == index) Color.White else Color.LightGray
                    )
                },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.title) {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.LightGray,
                alwaysShowLabel = false
            )
        }
    }
}

