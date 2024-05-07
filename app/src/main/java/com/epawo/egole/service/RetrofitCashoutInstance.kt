package com.epawo.egole.service

import com.epawo.egole.utilities.UrlConstants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitCashoutInstance {

    /**
     * Create an instance of Retrofit object
     */
    companion object{

        private var retrofit: Retrofit? = null

        fun getRetrofitInstance(): Retrofit? {
            if (retrofit == null) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                val builder = OkHttpClient.Builder()
                builder.connectTimeout(100, TimeUnit.SECONDS)
                builder.readTimeout(100, TimeUnit.SECONDS)
                builder.retryOnConnectionFailure(false)
                val okHttpClient = builder.addInterceptor(interceptor).build()

                retrofit = Retrofit.Builder()
                    .baseUrl(UrlConstants.CASHOUT_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()
            }
            return retrofit
        }
    }
}