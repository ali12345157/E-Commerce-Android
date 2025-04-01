package com.example.e_commerce_android
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.e_commerce_android.ui.theme.ECommerceAndroidTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyPassword:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ECommerceAndroidTheme {
                VerifyPassword()
            }
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyPassword(navController: NavController)
{

    var code by remember { mutableStateOf("") }
    var isEmailSent by remember { mutableStateOf(false) }
    val loading = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.main))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Verify Code ",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Enter your verify code that is send to your Gmail ",
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            placeholder = { Text("Enter your code") },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (code.isBlank()) {
                    Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                loading.value = true

                val request = VerifyResetCodeRequest(code)
                ApiManager.authService.verifyResetCode(request).enqueue(object :
                    Callback<VerifyResetCodeResponse> {
                    override fun onResponse(call: Call<VerifyResetCodeResponse>, response: Response<VerifyResetCodeResponse>) {
                        if (response.isSuccessful && response.body()?.status == "Success") {
                            Toast.makeText(context, "Process is successful!", Toast.LENGTH_SHORT).show()
                            loading.value=false
                           navController.navigate("New")


                        } else {
                            Toast.makeText(context, "Process is failed !", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<VerifyResetCodeResponse>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()

                    }
                })
            },
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            if (loading.value) {
                CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp))
            } else {
                Text("Verify Code ", color = Color.Black)
            }
        }

        if (isEmailSent) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Password reset link sent! Check your email.",
                color = Color.Green
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = { navController.navigate("login") }) {
            Text("Back to Login", color = Color.White)
        }
    }

}

