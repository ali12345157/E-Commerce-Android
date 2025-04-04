package com.example.e_commerce_android

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String,
    val rePassword:String,
    val phone:String
)


data class SignUpResponse(
    val message: String,
    val user: User,
    val token: String
)

data class User(
    val name: String,
    val email: String,
    val role: String
)

data class SignInRequest(
val email: String,
    val password:String

)
data class SignInResponse(
    val message: String,
    val user:User,
    val token:String

)


data class ForgetPassword(
    val email: String,

    )
data class ForgetPasswordResponse(
    val statusMsg:String,
    val message:String
)

data class VerifyResetCodeRequest(
    val resetCode: String
)

data class VerifyResetCodeResponse(
    val status: String,

)

data class NewPassRequest(
    val email:String,
    val newPassword:String
)

data class NewPassResponse(

    val statusMsg:String,
    val message:String
)


interface AuthService {

@POST("auth/signup")
fun signUp(@Body request: SignUpRequest):Call<SignUpResponse>

@POST("auth/signin")
fun signIn(@Body request: SignInRequest):Call<SignInResponse>

@POST("auth/forgotPasswords")
fun forget(@Body request:ForgetPassword):Call<ForgetPasswordResponse>

@POST("auth/verifyResetCode")
fun verifyResetCode(@Body request: VerifyResetCodeRequest): Call<VerifyResetCodeResponse>


@PUT("auth/resetPassword")
fun ResetPassword(@Body request:NewPassRequest):Call<NewPassResponse>
}


object RetrofitInstance {
    private const val BASE_URL = "https://ecommerce.routemisr.com/api/v1/"

    val api: AuthService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }
}