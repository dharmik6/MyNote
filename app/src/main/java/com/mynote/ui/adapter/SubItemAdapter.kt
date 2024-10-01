package com.mynote.ui.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.mynote.R
import com.mynote.data.param.NotesParam
import com.mynote.databinding.BuyingItemBinding
import com.mynote.databinding.GoalsItemBinding
import com.project.app.utils.extension.showSoftKeyboard
import dagger.Binds

class SubItemAdapter(
    private val context: Context,
    private var list: MutableList<NotesParam.Item.SubItem?>
) : RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder>() {

    private  val TAG = "SubItemAdapter"

    private var newlyAddedPosition: Int? = null

    private var onSubItemChangedListener: ((MutableList<NotesParam.Item.SubItem?>) -> Unit)? = null

    // ViewHolder for the sub-items
    class SubItemViewHolder(val binding: BuyingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(subItem: NotesParam.Item.SubItem?, onSubItemChanged: (NotesParam.Item.SubItem, Int) -> Unit, position: Int) {
            if (subItem != null) {
                // Set sub-item name


                // Set sub-item checkbox state
                binding.itemCheckbox.isChecked = subItem.isChecked ?: false

                if (subItem.isChecked) {
                    binding.itemText.paintFlags = binding.itemText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    binding.itemText.setText(subItem.name)

                } else {
                    binding.itemText.paintFlags = binding.itemText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    binding.itemText.setText(subItem.name)

                }

                // Handle checkbox toggle event for the sub-item
                binding.itemCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    subItem.isChecked = isChecked
                    onSubItemChanged(subItem, position) // Notify that the sub-item has changed

                    if (isChecked) {
                        binding.itemText.paintFlags = binding.itemText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        binding.itemText.setText(binding.itemText.text) // Set the text again to apply the change

                    } else {
                        binding.itemText.paintFlags = binding.itemText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                        binding.itemText.setText(binding.itemText.text) // Set the text again to apply the change

                    }
                }

                // Handle text change for the sub-item
                binding.itemText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        subItem.name = s.toString()
                        onSubItemChanged(subItem, position) // Notify that the sub-item has changed
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubItemViewHolder {
        val binding = BuyingItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return SubItemViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: SubItemViewHolder, position: Int) {
        holder.bind(list[position], this::updateSubItem, position)
        holder.binding.dragDrop.isGone = true

        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
        holder.itemView.startAnimation(animation)

        // Handle trash icon click to remove item
        holder.binding.trash.setOnClickListener {
            removeItem( position)
        }



        if (position == newlyAddedPosition) {
            holder.binding.itemText.requestFocus()
            holder.binding.itemText.post {
                if (holder.binding.itemText.hasFocus()) {
                    Log.d("BuyingItemAdapter", "View has gained focus, attempting to show keyboard.")
                    context.showSoftKeyboard(holder.binding.itemText)
                } else {
                    Log.d("BuyingItemAdapter", "View did not gain focus after request.")
                }
            }
            newlyAddedPosition = null // Reset the position after focusing
        }

        holder.binding.itemText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                Log.d(TAG, "Enter key pressed in onEditorActionListener")
                addSubItem(NotesParam.Item.SubItem())
                true
            } else {
                false
            }
        }

        holder.binding.itemText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (position > 0){
                    removeItem(position)
                    Log.d("KeyboardAction", "Backspace pressed at position: $position")
                }
                true
            } else {
                false
            }
        }
    }

    private fun removeItem( position: Int) {
        if (position < list.size) {
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, list.size)
        }

    }

    // Update a sub-item in the list
    private fun updateSubItem(subItem: NotesParam.Item.SubItem, position: Int) {
        list[position] = subItem
        onSubItemChangedListener?.invoke(list) // Notify the parent adapter of the changes
    }

    // Set listener for sub-item changes
    fun setOnSubItemChangedListener(listener: (MutableList<NotesParam.Item.SubItem?>) -> Unit) {
        this.onSubItemChangedListener = listener
    }

    // Add a new sub-item
    fun addSubItem(subItem: NotesParam.Item.SubItem) {
        list.add(subItem)
        val position = list.size - 1
        newlyAddedPosition = position
        notifyItemInserted(position)
        // Post a runnable to request focus after the item is added
        (context as? Activity)?.runOnUiThread {
            (context as? RecyclerView)?.post {
                notifyItemChanged(position)
            }
        }
    }

    // Retrieve the updated list of sub-items
    fun getSubItems(): MutableList<NotesParam.Item.SubItem?> {
        return list
    }
}
