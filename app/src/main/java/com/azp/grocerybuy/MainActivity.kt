package com.azp.grocerybuy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azp.grocerybuy.databinding.ActivityMainBinding
import com.azp.grocerybuy.model.Grocery
import com.azp.grocerybuy.view.GroceryAdapter
import com.azp.grocerybuy.view.GroceryViewHolder
import com.azp.grocerybuy.view.ItemDialogFragment
import com.azp.grocerybuy.viewmodel.GroceryViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(),
    ItemDialogFragment.NewItemDialogListener {

    lateinit var viewModel: GroceryViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(GroceryViewModel::class.java)

        binding.recyclerGrocery.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GroceryAdapter(
                viewModel.groceryItem,
                context, ::editGroceryItem, ::deleteGroceryItem
            )
        }

        binding.addItemButton.setOnClickListener {
            addGroceryItem()
        }
    }

    private fun addGroceryItem() {
        val newFragment = ItemDialogFragment.newInstance(R.string.dialog_title, null)
        newFragment.show(supportFragmentManager, "newItem")
    }

    private fun editGroceryItem(position: Int?) {
        val newFragment = ItemDialogFragment.newInstance(R.string.dialog_edit_title, position)
        newFragment.show(supportFragmentManager, "newItem")
    }

    private fun deleteGroceryItem(position: Int) {
        viewModel.removeItem(position)
        binding.totalAmount = viewModel.getTotal().toString()
        binding.recyclerGrocery.adapter?.notifyDataSetChanged()
    }

    override fun onDialogPositiveClick(
        dialog: DialogFragment,
        item: Grocery,
        isEdit: Boolean,
        position: Int?
    ) {
        if (!isEdit) {
            viewModel.groceryItem.add(item)
        } else {
            viewModel.updateItem(position!!, item)
            binding.recyclerGrocery.adapter?.notifyDataSetChanged()
        }
        binding.totalAmount = String.format("%.2f", viewModel.getTotal())
        Snackbar.make(
            binding.addItemButton, "Item Added Successfully",
            Snackbar.LENGTH_LONG
        ).setAction("Action", null).show()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Snackbar.make(
            binding.addItemButton, "Nothing Added",
            Snackbar.LENGTH_LONG
        ).setAction("Action", null).show()
    }
}