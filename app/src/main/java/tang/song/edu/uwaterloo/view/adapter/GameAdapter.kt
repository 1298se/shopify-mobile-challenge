package tang.song.edu.uwaterloo.view.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso
import tang.song.edu.uwaterloo.R
import tang.song.edu.uwaterloo.retrofit.models.ProductResponse

class GameAdapter(val items: List<ProductResponse>, val context: Context) : BaseAdapter() {

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val cardView: CardView
        if (convertView == null) {
            val layoutInflater =
                parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            cardView = layoutInflater.inflate(R.layout.card_product_image, parent, false) as CardView

            val imageView = cardView.findViewById<ImageView>(R.id.product_image)
            Picasso.get()
                .load(items[position].image.src)
                .centerCrop()
                .fit()
                .into(imageView)
        } else {
            cardView = convertView as CardView
        }
        return cardView
    }
}
