package tang.song.edu.uwaterloo.view.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.AdapterView
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_game.*
import tang.song.edu.uwaterloo.R
import tang.song.edu.uwaterloo.retrofit.models.ProductResponse
import tang.song.edu.uwaterloo.view.adapter.GameAdapter
import tang.song.edu.uwaterloo.viewModel.GameViewModel

class GameActivity : AppCompatActivity() {
    var gameViewModel: GameViewModel? = null
    var shouldAllowSelection = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val gameAdapter = GameAdapter(listOf(), listOf(), listOf())
        gameGridView.adapter = gameAdapter

        gameViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GameViewModel::class.java)
        val requiredMatches = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("game_required_matches","2") ?: "2")
        gameViewModel?.setRequiredMatches(requiredMatches)
        gameViewModel?.getData()?.observe(this, Observer { response ->
            if (response != null) {
                Log.d("CUR-TICKET", "getDataCallback")
                shouldAllowSelection = response.shouldAllowSelection

                scoreTextView.text = response.matchedProductsData.size.toString()

                gameAdapter.setProducts(response.productsData)
                gameAdapter.setMatchedProducts(response.matchedProductsData)
                gameAdapter.setSelectedPositions(response.selectedPositions)

                if (response.isAllMatched) {
                    AlertDialog.Builder(this)
                        .setTitle(R.string.game_complete)
                        .setMessage(R.string.game_complete_message)
                        .setNegativeButton(R.string.game_dismiss) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(R.string.main_menu) { _, _ ->
                            finish()
                        }
                }
            }
        })

        gameGridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, _ ->
            if (shouldAllowSelection) {
                val product = parent.adapter.getItem(position) as ProductResponse
                val imageView = view.findViewById<ImageView>(R.id.product_image)

                shouldAllowSelection = false
                Picasso.get().load(product.image.src)
                    .centerCrop()
                    .fit()
                    .into(imageView)
                gameViewModel?.setSelected(position)
            }
        }
        shuffleButton.setOnClickListener {
            Log.d("TAG", "TAGGED")
            gameViewModel?.shuffle()
        }
    }
}
