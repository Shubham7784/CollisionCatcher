package com.collisioncatcher.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import kotlin.jvm.Throws

class BasicAuthInterceptor(private val bToken : String): Interceptor {

    @Throws(IOException::class)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestChain = chain.request()
            .newBuilder()
            .addHeader("Authorization","Bearer ${bToken}")
            .build()
        return chain.proceed(requestChain)
    }
}