package com.sobodigital.zulbelajarandroid.data.remote
import com.sobodigital.zulbelajarandroid.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiConfig {


    companion object{
        private fun getLoggingInterceptor() : HttpLoggingInterceptor {
            if(BuildConfig.DEBUG) {
                return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            }
            return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        fun <T> getDataSource(serviceClass: Class<T>, token: String? = null): T {
            val loggingInterceptor = getLoggingInterceptor()

            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(requestHeaders)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://story-api.dicoding.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(serviceClass)
        }
    }
}