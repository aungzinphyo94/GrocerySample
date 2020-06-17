package com.azp.grocerybuy.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.azp.grocerybuy.R
import com.azp.grocerybuy.model.Grocery
import com.azp.grocerybuy.viewmodel.GroceryViewModel
import kotlinx.android.synthetic.main.dialog_item.*

class ItemDialogFragment : DialogFragment() {

    interface NewItemDialogListener {
        fun onDialogPositiveClick(
            dialog: DialogFragment,
            item: Grocery, isEdit: Boolean, position: Int?
        )

        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    private var newItemDialogListener: NewItemDialogListener? = null
    private var isEdit = false
    private lateinit var viewModel: GroceryViewModel

    companion object {
        fun newInstance(title: Int, position: Int?):
                ItemDialogFragment {
            val itemDialogFragment = ItemDialogFragment()
            val args = Bundle()
            args.putInt("dialog_title", title)
            if (position != null) {
                args.putInt("item_position", position)
            } else {
                args.putInt("item_position", -1)
            }
            itemDialogFragment.arguments = args
            return itemDialogFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        activity?.let {
            viewModel = ViewModelProvider(it).get(GroceryViewModel::class.java)
        }

        val title = arguments?.getInt("dialog_title")
        val position = arguments?.getInt("item_position", -1)

        val builder = AlertDialog.Builder(activity)
        if (title != null) {
            builder.setTitle(title)
        }

        newItemDialogListener = activity as NewItemDialogListener

        val dialogView = activity?.layoutInflater?.inflate(R.layout.dialog_item, null)

        val price = dialogView?.findViewById<EditText>(R.id.itemPrice)
        val amount = dialogView?.findViewById<EditText>(R.id.itemAmount)
        val name = dialogView?.findViewById<EditText>(R.id.itemName)

        if (position != -1) {
            val retrievedItem = viewModel.groceryItem[position!!]
            isEdit = true
            name?.setText(retrievedItem.itemName)
            amount?.setText(retrievedItem.amount.toString())
            price?.setText(retrievedItem.price.toString())
        }

        builder.setView(dialogView).setPositiveButton("Save") { _, _ ->

            val priceQuantity = price?.text.toString().toDouble()
            val amountOfItems = amount?.text.toString().toDouble()
            val total = priceQuantity * amountOfItems

            val item = Grocery(
                itemName = name?.text.toString(),
                price = priceQuantity,
                amount = amountOfItems.toInt(),
                finalPrice = total
            )

            newItemDialogListener?.onDialogPositiveClick(this, item, isEdit, position)
        }.setNegativeButton(android.R.string.cancel) { _, _ ->
            newItemDialogListener?.onDialogNegativeClick(this)
        }

        return builder.create()
    }
}