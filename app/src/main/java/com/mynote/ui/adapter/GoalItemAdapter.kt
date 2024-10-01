package com.mynote.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mynote.R
import com.mynote.data.param.NotesParam
import com.mynote.databinding.GoalsItemBinding
import com.mynote.ui.adapter.BuyingItemAdapter.ViewHolder
import com.project.app.utils.extension.showSoftKeyboard

class GoalItemAdapter(
    private val context: Context,
    private val itemTouchHelper: ItemTouchHelper
) : RecyclerView.Adapter<GoalItemAdapter.ViewHolder>() {

    private var newlyAddedPosition: Int? = null
    var selectedItem: Int? = null
    private  val TAG = "GoalItemAdapter"
    // ViewHolder for the main item
    class ViewHolder(val binding: GoalsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        // Bind the main item and its subitems
        fun bind(
            item: NotesParam.Item,
            context: Context
        ) {

            // Set main checkbox state
            binding.itemCheckbox.isChecked = item.isChecked
            if (item.isChecked) {
                binding.itemText.paintFlags = binding.itemText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.itemText.setText(item.name)

            } else {
                binding.itemText.paintFlags = binding.itemText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.itemText.setText(item.name)

            }

            // Handle checkbox toggle event for the main item
            binding.itemCheckbox.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                if (isChecked) {
                    binding.itemText.paintFlags = binding.itemText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    binding.itemText.setText(binding.itemText.text) // Set the text again to apply the change

                } else {
                    binding.itemText.paintFlags = binding.itemText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    binding.itemText.setText(binding.itemText.text) // Set the text again to apply the change

                }
            }

            // Handle text change for the main item
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

    override fun getItemCount() = differ.currentList.size

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item, context)

        holder.binding.trash.setOnClickListener {
            removeItem(position)
        }

        holder.binding.dragDrop.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                itemTouchHelper.startDrag(holder)
            }
            false
        }

        if (position == newlyAddedPosition) {
            if (!holder.binding.itemText.hasFocus()) {
                holder.binding.itemText.requestFocus()
                holder.binding.itemText.post {
                    context.showSoftKeyboard(holder.binding.itemText)
                }
                newlyAddedPosition = null
            }
        }

        holder.binding.itemText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                addItem(NotesParam.Item())
                true
            } else {
                false
            }
        }

        holder.binding.itemText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && position > 0) {
                removeItem(position)
                true
            } else {
                false
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return differ.currentList[position].uniqueId
    }

    fun addItem(item: NotesParam.Item) {
        val currentList = differ.currentList.toMutableList()
        currentList.add(item)
        differ.submitList(currentList) {
            newlyAddedPosition = currentList.size - 1 // Set the new position after the list is submitted
        }
    }

    fun removeItem(index: Int) {
        if (index < differ.currentList.size) {
            val currentList = differ.currentList.toMutableList()
            currentList.removeAt(index)
            differ.submitList(currentList) {
                notifyItemRemoved(index)
            }
        }
    }



    fun addItems(newItems: List<NotesParam.Item>) {
        val currentList = differ.currentList.toMutableList()  // Create a mutable copy of the current list
        currentList.addAll(newItems)                         // Add all new items
        differ.submitList(currentList)
    }


    // Retrieve the updated list
    fun getItems(): MutableList<NotesParam.Item> {
        return differ.currentList
    }



    private val differCallback = object : DiffUtil.ItemCallback<NotesParam.Item>() {

        // Check whether two items represent the same logical item, usually by comparing unique IDs
        override fun areItemsTheSame(oldItem: NotesParam.Item, newItem: NotesParam.Item): Boolean {
            return oldItem.name == newItem.name  // Assuming 'id' is a unique identifier for each item
        }

        // Check whether the content of two items is the same, which means their displayed data is the same
        override fun areContentsTheSame(oldItem: NotesParam.Item, newItem: NotesParam.Item): Boolean {
            return oldItem.name == newItem.name  // You can further optimize by comparing individual fields if needed
        }
    }


    val differ = AsyncListDiffer(this, differCallback)

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val list = differ.currentList.toMutableList()

        val fromItem = list.removeAt(fromPosition)  // Remove the item at fromPosition
        list.add(toPosition, fromItem)  // Add the item at toPosition

        differ.submitList(list)  // Submit the updated list to the adapter
    }


}
