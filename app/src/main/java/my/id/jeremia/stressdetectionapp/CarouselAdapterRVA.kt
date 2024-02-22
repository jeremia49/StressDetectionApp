package my.id.jeremia.stressdetectionapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class CarouselRVAdapter(private val carouselDataList: ArrayList<String>, val context:Context) :
    RecyclerView.Adapter<CarouselRVAdapter.CarouselItemViewHolder>() {

    class CarouselItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.itemcarousel, parent, false)
        return CarouselItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        val imageResource: Int = context.resources.getIdentifier(carouselDataList[position], null, context.packageName)

        val imageView = holder.itemView.findViewById(R.id.itemCarouselImage) as ImageView
        val res: Drawable = context.resources.getDrawable(imageResource)
        imageView.setImageDrawable(res)
    }

    override fun getItemCount(): Int {
        return carouselDataList.size
    }

}