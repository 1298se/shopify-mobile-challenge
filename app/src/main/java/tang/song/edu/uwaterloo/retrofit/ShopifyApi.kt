package tang.song.edu.uwaterloo.retrofit

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Query
import tang.song.edu.uwaterloo.retrofit.models.ShopifyProductsResponse

interface ShopifyApi {
    @GET("/admin/products.json")
    fun getProducts(@Query("page") page: Int=1,
                    @Query("access_token") token: String="c32313df0d0ef512ca64d5b336a0d7c6"):
            Call<ShopifyProductsResponse>
}