package tang.song.edu.uwaterloo.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tang.song.edu.uwaterloo.retrofit.RetrofitService
import tang.song.edu.uwaterloo.retrofit.models.ProductResponse
import tang.song.edu.uwaterloo.retrofit.models.ShopifyProductsResponse
import java.util.*

class GameViewModel : ViewModel() {
    var data: MutableLiveData<GameViewModelData>? = null
    var matchedProducts: MutableList<ProductResponse> = mutableListOf()
    var products: MutableList<ProductResponse> = mutableListOf()
    var selectedPositions: MutableList<Int> = mutableListOf()

    fun getData(): LiveData<GameViewModelData> {
        Log.d("CUR-TICKET", "getData()")
        if (data?.value?.productsData == null) {
            data = MutableLiveData()
            fetchApiData()
        }
        return data as LiveData<GameViewModelData>
    }

    fun setSelected(pos: Int) {
        if (!matchedProducts.contains(products[pos])) {
            var shouldAllowSelection = true
            when {
                selectedPositions.size < 1 -> {
                    selectedPositions.add(pos)
                }
                selectedPositions.size == 1 -> {
                    when {
                        selectedPositions[0] == pos -> {
                            selectedPositions.remove(pos)
                        }
                        products[selectedPositions[0]] == products[pos] -> {
                            matchedProducts.add(products[pos])
                            selectedPositions = mutableListOf()
                        }
                        else -> {
                            selectedPositions.add(pos)
                            shouldAllowSelection = false

                            Handler().postDelayed({
                                selectedPositions = mutableListOf()
                                val dataInstance = data?.value?.copy()
                                dataInstance?.selectedPositions = selectedPositions
                                dataInstance?.shouldAllowSelection = true
                                data?.postValue(dataInstance)
                            }, 1000)
                        }
                    }
                }
            }

            val dataInstance = GameViewModelData(
                products,
                matchedProducts,
                selectedPositions,
                shouldAllowSelection
            )
            data?.postValue(dataInstance)
        }
    }

    fun shuffle() {
        val matchedPositions = mutableListOf<Int>()
        matchedProducts.forEach { matchedProduct ->
            for ((index, value) in products.withIndex()) {
                if (matchedProduct == value) {
                    matchedPositions.add(index)
                }
            }
        }

        products.shuffle()

        val shuffledMatchedPositions = mutableListOf<Int>()
        matchedProducts.forEach { matchedProduct ->
            for ((index, value) in products.withIndex()) {
                if (matchedProduct == value) {
                    shuffledMatchedPositions.add(index)
                }
            }
        }

        for ((index, value) in matchedPositions.withIndex()) {
            Collections.swap(products, value, shuffledMatchedPositions[index])
        }
        val dataInstance = data?.value?.copy()
        dataInstance?.productsData = products

        data?.postValue(dataInstance)
    }

    fun fetchApiData() {
        val call = RetrofitService.createService().getProducts()
        Log.d("CUR-TICKET", "api call")
        call.enqueue(object : Callback<ShopifyProductsResponse> {
            override fun onResponse(
                call: Call<ShopifyProductsResponse>,
                response: Response<ShopifyProductsResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("CUR-TICKET", "call is successful")
                    products = response.body()?.products as MutableList<ProductResponse>
                    products.addAll(products)
                    products.shuffle()
                    val dataInstance =
                        GameViewModelData(products, matchedProducts, selectedPositions, true)

                    data?.postValue(dataInstance)
                }
            }

            override fun onFailure(call: Call<ShopifyProductsResponse>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}
