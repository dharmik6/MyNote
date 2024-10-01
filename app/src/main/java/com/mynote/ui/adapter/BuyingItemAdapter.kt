package com.mynote.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mynote.data.param.NotesParam
import com.mynote.databinding.BuyingItemBinding
import com.project.app.utils.extension.showSoftKeyboard

class BuyingItemAdapter(
    private val context: Context,
    private val itemTouchHelper: ItemTouchHelper
) : RecyclerView.Adapter<BuyingItemAdapter.ViewHolder>() {

    var selectedItem: Int? = null
    private val TAG = "BuyingItemAdapter"

    private var newlyAddedPosition: Int? = null

    class ViewHolder(val binding: BuyingItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private val TAG = "BuyingItemAdapter"
        fun bind(
            item: NotesParam.Item
        ) {

            binding.itemCheckbox.isChecked = item.isChecked ?: false

            if (item.isChecked) {
                binding.itemText.paintFlags = binding.itemText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.itemText.setText(item.name)

            } else {
                binding.itemText.paintFlags = binding.itemText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.itemText.setText(item.name)

            }

            // Add the listener back with the updated item
            binding.itemCheckbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    binding.itemText.paintFlags = binding.itemText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    binding.itemText.setText(binding.itemText.text) // Set the text again to apply the change
                    Log.d(TAG, "bind: Strikethrough text")
                } else {
                    binding.itemText.paintFlags = binding.itemText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    binding.itemText.setText(binding.itemText.text) // Set the text again to apply the change
                    Log.d(TAG, "bind: normal text")
                }

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

    override fun getItemId(position: Int): Long {
        return differ.currentList[position].uniqueId
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BuyingItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = differ.currentList.size

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)


        holder.binding.trash.setOnClickListener {
            removeItem(position)
        }


        if (position == newlyAddedPosition) {
            holder.binding.itemText.requestFocus()
            holder.binding.itemText.post {
                if (holder.binding.itemText.hasFocus()) {
                    Log.d(
                        "BuyingItemAdapter",
                        "View has gained focus, attempting to show keyboard."
                    )
                    context.showSoftKeyboard(holder.binding.itemText)
                } else {
                    Log.d("BuyingItemAdapter", "View did not gain focus after request.")
                }
            }
            newlyAddedPosition = null
        }
        holder.binding.itemText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                Log.d(TAG, "Enter key pressed in onEditorActionListener")
                addItem(NotesParam.Item())
                true
            } else {
                false
            }
        }

        holder.binding.dragDrop.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                itemTouchHelper.startDrag(holder)  // Start drag when handle is touched
            }
            false
        }
        holder.binding.itemText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (position > 0) {
                    removeItem(position)
                    Log.d("KeyboardAction", "Backspace pressed at position: $position")
                }
                true
            } else {
                false
            }
        }


    }

    fun removeItem(index: Int) {
        val currentList = differ.currentList.toMutableList()
        currentList.removeAt(index)
        differ.submitList(currentList)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, differ.currentList.size)
    }


    fun addItem(item: NotesParam.Item) {
        val currentList = differ.currentList.toMutableList()
        currentList.add(item)
        differ.submitList(currentList)
        val position = currentList.size - 1
        selectedItem = position
        newlyAddedPosition = position
    }

    fun addItems(newItems: List<NotesParam.Item>) {
        val currentList =
            differ.currentList.toMutableList()  // Create a mutable copy of the current list
        currentList.addAll(newItems)                         // Add all new items
        differ.submitList(currentList)
    }

    // Retrieve the updated list
    fun getItems(): MutableList<NotesParam.Item> {
        return differ.currentList
    }

    private val differCallback = object : DiffUtil.ItemCallback<NotesParam.Item>() {


        override fun areItemsTheSame(oldItem: NotesParam.Item, newItem: NotesParam.Item): Boolean {
            return oldItem.name == newItem.name
        }


        override fun areContentsTheSame(
            oldItem: NotesParam.Item,
            newItem: NotesParam.Item
        ): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, differCallback)

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val list = differ.currentList.toMutableList()

        val fromItem = list.removeAt(fromPosition)
        list.add(toPosition, fromItem)

        differ.submitList(list)
    }


}
