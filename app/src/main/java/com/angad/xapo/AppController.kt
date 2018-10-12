package com.angad.xapo

import android.app.Application
import com.angad.xapo.networks.AndroidTrendingRepoService
import com.facebook.drawee.backends.pipeline.Fresco
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class AppController: Application() {

    override fun onCreate() {
        super.onCreate()

        Fresco.initialize(this);
        initRetrofit2()
    }

    companion object {
        var service: AndroidTrendingRepoService? = null

        fun getRetrofitService(): AndroidTrendingRepoService? {
            if(service == null) {
                initRetrofit2()
            }
            return service
        }

        private fun initRetrofit2() {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            service = retrofit.create<AndroidTrendingRepoService>(AndroidTrendingRepoService::class.java)
        }
    }
}