package com.mynote.ui.adapter

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.mynote.R
import com.mynote.data.param.NotesParam
import com.mynote.databinding.BuyingItemBinding
import com.mynote.databinding.GoalsItemBinding

class SubItemAdapter(
    private val context: Context,
    private var list: MutableList<NotesParam.Item.SubItem?>
) : RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder>() {

    private var newlyAddedPosition: Int? = null

    private var onSubItemChangedListener: ((MutableList<NotesParam.Item.SubItem?>) -> Unit)? = null

    // ViewHolder for the sub-items
    class SubItemViewHolder(val binding: BuyingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(subItem: NotesParam.Item.SubItem?, onSubItemChanged: (NotesParam.Item.SubItem, Int) -> Unit, position: Int) {
            if (subItem != null) {
                // Set sub-item name
                binding.itemText.setText(subItem.name)

                // Set sub-item checkbox state
                binding.itemCheckbox.isChecked = subItem.isChecked ?: false

                // Handle checkbox toggle event for the sub-item
                binding.itemCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    subItem.isChecked = isChecked
                    onSubItemChanged(subItem, position) // Notify that the sub-item has changed
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

        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
        holder.itemView.startAnimation(animation)

        // Handle trash icon click to remove item
        holder.binding.trash.setOnClickListener {
            removeItem(holder.itemView , position)
        }


        if (position == newlyAddedPosition) {
            holder.binding.itemText.requestFocus()
            newlyAddedPosition = null // Reset the position after focusing
        }
    }

    private fun removeItem(view: View, position: Int) {
        if (position < list.size) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
            view.startAnimation(animation)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // If you don't need to do anything at the start of the animation, leave this empty.
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // Remove the item after the animation ends
                    list.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, list.size)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // If not needed, leave this empty too
                }
            })
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
