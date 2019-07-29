package me.jerryhanks.kudatest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


/**
 * @author jerry on 2019-07-28
 * for KudaTest
 **/
class HeaderViewHolder(val containerView: View)  : RecyclerView.ViewHolder(containerView){

    fun bindItem(position: Int) {

        containerView.findViewById<TextView>(R.id.tvHeader).text = Util.getHeaderText(position)
    }

    companion object{
        fun create(parent: ViewGroup) : HeaderViewHolder{
            val inflater = LayoutInflater.from(parent.context)

            val rootView = inflater.inflate(R.layout.header_view,parent,false)
            return HeaderViewHolder(rootView)
        }
    }
}