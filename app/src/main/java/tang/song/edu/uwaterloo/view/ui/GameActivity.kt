package tang.song.edu.uwaterloo.view.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_game.*
import tang.song.edu.uwaterloo.R
import tang.song.edu.uwaterloo.retrofit.models.ProductResponse
import tang.song.edu.uwaterloo.view.adapter.GameAdapter
import tang.song.edu.uwaterloo.viewModel.GameViewModel

class GameActivity : AppCompatActivity() {
    var products: List<ProductResponse>? = null
    var gameViewModel: GameViewModel? = null
    val TAG = GameViewModel::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GameViewModel::class.java)
        gameViewModel?.getData()?.observe(this, Observer { response ->
            products = response
            val productList = products
            if (productList != null) {
                Log.d("TAG", productList.toString())
                val gameAdapter = GameAdapter(productList, this)

                gameGridView.adapter = gameAdapter
            }
        })

        shuffleButton.setOnClickListener {
            Log.d("TAG", "TAGGED")
            gameViewModel?.shuffle()
        }
    }
}
