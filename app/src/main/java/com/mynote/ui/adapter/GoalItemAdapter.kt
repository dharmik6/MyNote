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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mynote.R
import com.mynote.data.param.NotesParam
import com.mynote.databinding.GoalsItemBinding

class GoalItemAdapter(
    private val context: Context,
    private var list: MutableList<NotesParam.Item>
) : RecyclerView.Adapter<GoalItemAdapter.ViewHolder>() {

    private var newlyAddedPosition: Int? = null

    // ViewHolder for the main item
    class ViewHolder(val binding: GoalsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        // Bind the main item and its subitems
        fun bind(
            item: NotesParam.Item,
            onItemChanged: (NotesParam.Item, Int) -> Unit,
            position: Int,
            context: Context
        ) {
            // Set main item name
            binding.itemText.setText(item.name)


            // Set main checkbox state
            binding.itemCheckbox.isChecked = item.isChecked ?: false

            // Handle checkbox toggle event for the main item
            binding.itemCheckbox.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                onItemChanged(item, position) // Notify that the main item has changed
            }

            // Handle text change for the main item
            binding.itemText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    item.name = s.toString()
                    onItemChanged(item, position) // Notify that the main item has changed
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

            // Set up the RecyclerView for SubItems (nested items)
            val subItemAdapter = SubItemAdapter(
                context,
                (item.subItems ?: mutableListOf()).toMutableList()
            )
            binding.rvSubItems.layoutManager = LinearLayoutManager(context)
            binding.rvSubItems.adapter = subItemAdapter

            // Notify when SubItems are updated
            subItemAdapter.setOnSubItemChangedListener { updatedSubItems ->
                item.subItems = updatedSubItems // Update subItems in the main item
                onItemChanged(item, position)
            }
            binding.addSubItem.setOnClickListener {
                val newItem = NotesParam.Item.SubItem(isChecked = false, name = "")
                subItemAdapter.addSubItem(newItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GoalsItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], this::updateItem, position, context)

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

    // Update an item in the list
    private fun updateItem(item: NotesParam.Item, position: Int) {
        list[position] = item
    }

    // Add a new main item to the list
    fun addItem(item: NotesParam.Item) {
        list.add(item)
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

    private fun removeItem(view: View, position: Int) {
        if ( position >= 0 && position < list.size) {
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
