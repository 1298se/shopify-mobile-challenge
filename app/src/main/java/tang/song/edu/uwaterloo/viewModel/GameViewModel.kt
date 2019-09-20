package tang.song.edu.uwaterloo.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.util.Log
import tang.song.edu.uwaterloo.retrofit.models.ProductResponse
import tang.song.edu.uwaterloo.retrofit.repository.ProductRepository

class GameViewModel : ViewModel() {
    val TAG = GameViewModel::class.java.simpleName
    var products: List<ProductResponse>? = null
    var productsData: MutableLiveData<List<ProductResponse>>? = ProductRepository.getProducts()


    fun getData(): LiveData<List<ProductResponse>> {


        return if (productsData?.value != null) {
            productsData as LiveData<List<ProductResponse>>
        } else {
            Log.d(TAG, "returning transformation")
            val liveData = Transformations.map(productsData as LiveData<List<ProductResponse>>) { newData ->
                products = newData
                newData.shuffled()
            }

            productsData = liveData as MutableLiveData<List<ProductResponse>>
            productsData as LiveData<List<ProductResponse>>
        }
    }

    fun shuffle() {
        Log.d(TAG, "shuffling")
        productsData?.value = products?.shuffled()
    }
}
