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

class GameAdapter(
    items: List<ProductResponse>,
    selectedItems: List<ProductResponse>,
    selectedPositions: List<Int>
) :
    BaseAdapter() {
    var mMatchedItems = selectedItems
    var mItems = items
    var mCurrentPositions = selectedPositions

    override fun getItem(position: Int): Any {
        return mItems[position]
    }

    override fun getItemId(position: Int): Long {
        return mItems[position].id.toLong()
    }

    override fun getCount(): Int {
        return mItems.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var cardView = convertView
        val viewHolder: ViewHolder?

        if (cardView == null) {
            val layoutInflater =
                parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            cardView =
                layoutInflater.inflate(R.layout.card_product_image, parent, false) as CardView
            viewHolder = ViewHolder(cardView)
            cardView.tag = viewHolder
        } else {
            cardView.forceLayout()
            viewHolder = cardView.tag as ViewHolder
        }
        val currentItem = mItems[position]

        if (mMatchedItems.contains(currentItem) || mCurrentPositions.contains(position)) {
            Picasso.get()
                .load(currentItem.image.src)
                .centerCrop()
                .fit()
                .into(viewHolder.mImageView)
        } else {
            Picasso.get()
                .load(android.R.color.transparent)
                .centerCrop()
                .fit()
                .into(viewHolder.mImageView)
        }
        return cardView
    }

    fun setProducts(items: List<ProductResponse>) {
        mItems = items
        notifyDataSetChanged()
    }

    fun setMatchedProducts(items: List<ProductResponse>) {
        mMatchedItems = items
        notifyDataSetChanged()
    }

    fun setSelectedPositions(positions: List<Int>) {
        mCurrentPositions = positions
        notifyDataSetChanged()
    }

    private class ViewHolder(cardView: CardView) {
        val mImageView: ImageView = cardView.findViewById(R.id.product_image)
    }
}
