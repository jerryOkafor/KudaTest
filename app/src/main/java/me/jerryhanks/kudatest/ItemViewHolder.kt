package me.jerryhanks.kudatest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*


/**
 * @author jerry on 2019-07-28
 * for KudaTest
 **/

class ItemViewHolder(val containerView:View)  : RecyclerView.ViewHolder(containerView){

    fun bindItem(position: Int) {

        containerView.findViewById<TextView>(R.id.tvTitle).text = "Item $position"

        val imageView = containerView.findViewById<ImageView>(R.id.ivIcon)

        val random = Random()
        when (random.nextInt(5)) {
            0 -> imageView.setImageResource(R.drawable.icon1)
            1 -> imageView.setImageResource(R.drawable.icon2)
            2 -> imageView.setImageResource(R.drawable.icon3)
            3 -> imageView.setImageResource(R.drawable.icon4)
            4 -> imageView.setImageResource(R.drawable.icon5)
        }
    }
    companion object{
        fun create(parent:ViewGroup) : ItemViewHolder{
            val inflater = LayoutInflater.from(parent.context)

            val rootView = inflater.inflate(R.layout.item_view,parent,false)
            return ItemViewHolder(rootView)
        }
    }
}