package com.azp.grocerybuy.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.azp.grocerybuy.R
import com.azp.grocerybuy.databinding.ItemGroceryBinding
import com.azp.grocerybuy.model.Grocery

class GroceryAdapter(
    val items: ArrayList<Grocery>,
    val context: Context,
    val itemEditListener: (position: Int) -> Unit,
    val itemDeleteListener: (position: Int) -> Unit
) : RecyclerView.Adapter<GroceryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding: ItemGroceryBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_grocery, parent, false
        )
        return GroceryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {

        val item = items[position]
        val description = item.amount.toString() + " : " + item.itemName
        holder.bind(items[position])
        holder.binding.buttonEdit.setOnClickListener {
            itemEditListener(position)
        }
        holder.binding.buttonDelete.setOnClickListener {
            itemDeleteListener(position)
        }
    }
}

class GroceryViewHolder(val binding: ItemGroceryBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Grocery) {
        binding.apply {
            itemName = "${item.amount} : ${item.itemName}"
            price = item.price.toString()
        }
    }
}