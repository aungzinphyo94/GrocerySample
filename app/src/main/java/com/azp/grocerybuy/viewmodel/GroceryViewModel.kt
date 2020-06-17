package com.azp.grocerybuy.viewmodel

import androidx.lifecycle.ViewModel
import com.azp.grocerybuy.model.Grocery

class GroceryViewModel: ViewModel() {

    var groceryItem: ArrayList<Grocery> = ArrayList()

    fun getTotal(): Double = groceryItem.map {
        it.finalPrice
    }.sum()

    fun removeItem(position: Int) {
        groceryItem.removeAt(position)
    }

    fun updateItem(position: Int, updatedItem: Grocery) {
        groceryItem[position] = updatedItem
    }

}