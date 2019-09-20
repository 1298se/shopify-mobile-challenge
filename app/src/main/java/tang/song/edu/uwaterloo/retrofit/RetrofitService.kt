package tang.song.edu.uwaterloo.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    companion object {
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://shopicruit.myshopify.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        fun createService(): ShopifyApi {
            return retrofit.create(ShopifyApi::class.java)
        }
    }
}