package com.smaildahmani.quickshop.api

import android.content.Context
import android.util.Base64
import com.smaildahmani.quickshop.util.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val API_BASE_URL = "$BASE_URL/api/"

    fun getInstance(context: Context): Retrofit {
        val sharedPref = context.getSharedPreferences("QuickShop", Context.MODE_PRIVATE)
        val email = sharedPref.getString("EMAIL", null)
        val password = sharedPref.getString("PASSWORD", null)

        val client = if (email != null && password != null) {
            val credentials = "Basic " + Base64.encodeToString("$email:$password".toByteArray(), Base64.NO_WRAP)

            OkHttpClient.Builder()
                .addInterceptor { chain: Interceptor.Chain ->
                    val request: Request = chain.request().newBuilder()
                        .addHeader("Authorization", credentials)
                        .build()
                    chain.proceed(request)
                }
                .build()
        } else {
            OkHttpClient.Builder().build()
        }

        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object ApiClient {
    fun getApiService(context: Context): ApiService {
        return RetrofitClient.getInstance(context).create(ApiService::class.java)
    }
}
