package gq.kirmanak.mealient.datasource

import okhttp3.Interceptor
import okhttp3.OkHttpClient

/**
 * Marker interface which is different from [Interceptor] only in how it is handled.
 * [Interceptor]s are added as network interceptors to OkHttpClient whereas [LocalInterceptor]s
 * are added via [OkHttpClient.Builder.addInterceptor] function. They will observe the
 * full call lifecycle, whereas network interceptors will see only the network part.
 */
interface LocalInterceptor : Interceptor