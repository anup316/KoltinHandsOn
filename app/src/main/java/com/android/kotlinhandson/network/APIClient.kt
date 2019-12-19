package com.android.kotlinhandson.network;

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object APIClient {

     lateinit var retrofit: Retrofit
     fun <T> getService(service : Class<out T>) : T {

          val logger = HttpLoggingInterceptor()
          logger.level = HttpLoggingInterceptor.Level.BODY
          val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(logger).build()


          retrofit = Retrofit.Builder()
               .baseUrl("http://5de91777cb3e3800141b8d7d.mockapi.io/")
               .addConverterFactory(GsonConverterFactory.create())
               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
               .client(okHttpClient)
               .build()

        return retrofit.create(service)

     }
}
