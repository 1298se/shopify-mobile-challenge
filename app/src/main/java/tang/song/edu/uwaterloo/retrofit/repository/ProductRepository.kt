package tang.song.edu.uwaterloo.retrofit.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tang.song.edu.uwaterloo.retrofit.RetrofitService
import tang.song.edu.uwaterloo.retrofit.ShopifyApi
import tang.song.edu.uwaterloo.retrofit.models.ProductResponse
import tang.song.edu.uwaterloo.retrofit.models.ShopifyProductsResponse

class ProductRepository {
    companion object {
        private var api: ShopifyApi = RetrofitService.createService()

        fun getProducts(): MutableLiveData<List<ProductResponse>> {
            val productsData = MutableLiveData<List<ProductResponse>>()
            api.getProducts().enqueue(object : Callback<ShopifyProductsResponse> {
                override fun onResponse(
                    call: Call<ShopifyProductsResponse>,
                    response: Response<ShopifyProductsResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("TAG", response.body()?.products.toString())
                        productsData.value = response.body()?.products
                    }
                }

                override fun onFailure(call: Call<ShopifyProductsResponse>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })

            return productsData
        }
    }
}