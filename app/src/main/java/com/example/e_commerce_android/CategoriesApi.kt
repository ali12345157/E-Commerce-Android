package com.example.e_commerce_android
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesApi : ViewModel() {

    var categories = mutableStateListOf<DataItem>()


    var isLoading by mutableStateOf(false)

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        isLoading = true
        RetrofitInstance.api.GetCategories().enqueue(object : Callback<ResponseCategories> {
            override fun onResponse(call: Call<ResponseCategories>, response: Response<ResponseCategories>) {
                isLoading = false
                if (response.isSuccessful) {
                    response.body()?.data?.filterNotNull()?.let {
                        categories.clear()
                        categories.addAll(it)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseCategories>, t: Throwable) {
                isLoading = false
                println(t.message)
            }
        })
    }
}



