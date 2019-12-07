package com.android.kotlinhandson.network;

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {

     lateinit var retrofit: Retrofit
     fun getService() : NotesService {

          val logger = HttpLoggingInterceptor()
          logger.level = HttpLoggingInterceptor.Level.BODY
          val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(logger).build()


          retrofit = Retrofit.Builder()
               .baseUrl("http://5de91777cb3e3800141b8d7d.mockapi.io/")
               .addConverterFactory(GsonConverterFactory.create())
               .client(okHttpClient)
               .build()

        return retrofit.create(NotesService::class.java)

     }
}
