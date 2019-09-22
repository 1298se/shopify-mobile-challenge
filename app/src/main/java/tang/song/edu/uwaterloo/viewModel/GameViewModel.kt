package tang.song.edu.uwaterloo.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tang.song.edu.uwaterloo.retrofit.RetrofitService
import tang.song.edu.uwaterloo.retrofit.models.ProductResponse
import tang.song.edu.uwaterloo.retrofit.models.ShopifyProductsResponse

class GameViewModel : ViewModel() {
    var data: MutableLiveData<GameViewModelData>? = null
    var matchedProducts: MutableList<ProductResponse> = mutableListOf()
    var products: MutableList<ProductResponse> = mutableListOf()
    var selectedPositions: MutableList<Int> = mutableListOf()
    var isAllMatched = false

    var errorString: MutableLiveData<String>? = null
    private var requiredMatches = 0

    fun getData(): LiveData<GameViewModelData> {
        if (data?.value?.productsData == null) {
            data = MutableLiveData()
            fetchApiData()
        }
        return data as LiveData<GameViewModelData>
    }

    fun getError(): LiveData<String> {
        if (errorString == null) {
            errorString = MutableLiveData()
        }
        return errorString as LiveData<String>
    }

    fun setSelected(pos: Int) {
        if (!matchedProducts.contains(products[pos])) {
            var shouldAllowSelection = true
            when {
                selectedPositions.contains(pos) -> {
                    selectedPositions.remove(pos)
                }
                selectedPositions.size < requiredMatches - 1 -> {
                    selectedPositions.add(pos)
                }
                selectedPositions.size == requiredMatches - 1 -> {
                    when {
                        areAllEqual(selectedPositions) && (products[pos] == products[selectedPositions[0]]) -> {
                            matchedProducts.add(products[pos])
                            selectedPositions = mutableListOf()

                            if (matchedProducts.size == products.size / requiredMatches) {
                                isAllMatched = true
                            }
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
                shouldAllowSelection,
                isAllMatched
            )
            data?.postValue(dataInstance)
        }
    }

    fun shuffle() {
        val unfixedPositions = mutableListOf<Int>()
        val unfixedValues = mutableListOf<ProductResponse>()

        for ((index, value) in products.withIndex()) {
            if (!matchedProducts.contains(value)) {
                unfixedPositions.add(index)
                unfixedValues.add(value)
            }
        }

        unfixedPositions.shuffle()

        products = products.mapIndexed { index, product ->
            if (matchedProducts.contains(product)) {
                product
            } else {
                unfixedValues[unfixedPositions.indexOf(index)]
            }
        } as MutableList

        val dataInstance = data?.value?.copy()
        dataInstance?.productsData = products

        data?.postValue(dataInstance)
    }

    private fun fetchApiData() {
        val call = RetrofitService.createService().getProducts()
        call.enqueue(object : Callback<ShopifyProductsResponse> {
            override fun onResponse(
                call: Call<ShopifyProductsResponse>,
                response: Response<ShopifyProductsResponse>
            ) {
                if (!response.isSuccessful) {
                    errorString?.postValue("An unexpected error occurred: " + response.message())
                    return
                }
                val baseProducts =
                    response.body()?.products?.shuffled()?.take(10) as MutableList<ProductResponse>
                repeat(requiredMatches) {
                    products.addAll(baseProducts)
                }
                products.shuffle()
                val dataInstance =
                    GameViewModelData(
                        products,
                        matchedProducts,
                        selectedPositions,
                        true,
                        isAllMatched
                    )

                data?.postValue(dataInstance)
            }

            override fun onFailure(call: Call<ShopifyProductsResponse>, t: Throwable) {
                errorString?.postValue("An unexpected error occurred: " + t.message)
            }
        })
    }

    fun setRequiredMatches(num: Int) {
        requiredMatches = num
    }

    private fun areAllEqual(list: MutableList<Int>): Boolean {
        val mappedProducts = list.map { pos ->
            products[pos]
        }
        val firstProduct = mappedProducts[0]
        for (product in mappedProducts) {
            if (product != firstProduct) {
                return false
            }
        }

        return true
    }
}
