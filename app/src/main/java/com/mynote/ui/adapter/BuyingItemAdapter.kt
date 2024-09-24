package com.mynote.ui.adapter

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mynote.data.param.NotesParam
import com.mynote.databinding.BuyingItemBinding

class BuyingItemAdapter(
    private val context: Context,
    private var list: MutableList<NotesParam.Item>
) : RecyclerView.Adapter<BuyingItemAdapter.ViewHolder>() {

    var selectedItem: Int? = null



    class ViewHolder(val binding: BuyingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: NotesParam.Item
        ) {
            // Remove the listener to avoid unnecessary triggers
            binding.itemCheckbox.setOnCheckedChangeListener(null)

            // Set the item name and checkbox state
            binding.itemText.setText(item.name)
            binding.itemCheckbox.isChecked = item.isChecked ?: false

            // Add the listener back with the updated item
            binding.itemCheckbox.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
            }

            // Update list when the name is changed
            binding.itemText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    item.name = s.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BuyingItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)

        holder.binding.trash.setOnClickListener {
            removeItem(position)
        }
    }

    fun removeItem(index: Int) {
        if (index >= 0 && index < list.size) { // Validate the index
            list.removeAt(index)
            notifyItemRemoved(index)
            notifyItemRangeChanged(index, list.size)
        } else {
            Log.e("BuyingItemAdapter", "Invalid index: $index, Size: ${list.size}")
        }
    }


    fun addItem(item: NotesParam.Item) {
        list.add(item)
        val position = list.size - 1
        selectedItem = position
        notifyItemInserted(position)
        // No need for additional runOnUiThread or post
    }


    fun addItems(newItems: List<NotesParam.Item>) {
        val startPosition = list.size
        list.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    // Retrieve the updated list
    fun getItems(): MutableList<NotesParam.Item> {
        return list
    }


}
