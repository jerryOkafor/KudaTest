package me.jerryhanks.kudatest

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


/**
 * @author jerry on 2019-07-28
 * for KudaTest
 **/

class TransactionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),StickyHeader<RecyclerView.ViewHolder> {

    var items = arrayListOf<Section>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when (viewType) {
            Section.HEADER,Section.CUSTOM_HEADER -> HeaderViewHolder.create(parent)
            else -> ItemViewHolder.create(parent)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type()
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = items[position].type()
        val section = items[position].sectionPosition()
        when (type) {
            Section.HEADER -> (holder as HeaderViewHolder).bindItem(section)
            Section.ITEM -> (holder as ItemViewHolder).bindItem(section)
            else -> (holder as HeaderViewHolder).bindItem(section)
        }
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        return items[itemPosition].sectionPosition()

    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder, headerPosition: Int) {
        (holder as HeaderViewHolder).bindItem(headerPosition)

    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return createViewHolder(parent,Section.HEADER)
    }
}