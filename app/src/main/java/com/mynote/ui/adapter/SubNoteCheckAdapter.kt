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
import com.mynote.databinding.NotesSubItemBinding
import com.mynote.interfaces.OnSubNoteUnCheckedChangeListener

class SubNoteCheckAdapter(
    private val context: Context,
    private var list: MutableList<NotesParam.SubNote>,
    private val listener: OnSubNoteUnCheckedChangeListener

) : RecyclerView.Adapter<SubNoteCheckAdapter.ViewHolder>() {

    private val TAG = "SubNoteCheckAdapter"



    class ViewHolder(val binding: NotesSubItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            subNote: NotesParam.SubNote
        ) {
            // Remove the listener to avoid unnecessary triggers
            binding.checkbox.setOnCheckedChangeListener(null)

            // Set the item name and checkbox state
            binding.titleText.setText(subNote.title)
            binding.noteText.setText(subNote.noteText)
            binding.checkbox.isChecked = subNote.isChecked ?: false

            // Add the listener back with the updated item
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                subNote.isChecked = isChecked
            }

            // Update list when the name is changed
            binding.titleText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    subNote.title = s.toString()
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
            binding.noteText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    subNote.noteText = s.toString()
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
        val binding = NotesSubItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)

        holder.binding.deleteSubNote.setOnClickListener {
            removeItem(holder.itemView,position)
        }

        holder.binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            item.isChecked = isChecked
            Log.d(TAG, "onBindViewHolder: item data = $item")
            changeData(listener, position,item)
        }
    }


    private fun changeData(listener: OnSubNoteUnCheckedChangeListener, position: Int, item : NotesParam.SubNote) {
        if (position < list.size) {
            val subNote = NotesParam.SubNote(
                isChecked = item.isChecked,
                noteText = item.noteText,
                title = item.title
            )
            listener.onSubNoteUnCheckedChangeListener(subNote)

            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, list.size)
        }
    }

    fun addItem(item: NotesParam.SubNote) {
        list.add(item)
        notifyItemInserted(list.size - 1)
    }

    fun addItems(newItems: List<NotesParam.SubNote>) {
        val startPosition = list.size
        list.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    private fun removeItem(view: View, position: Int) {
        if (position < list.size) {
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, list.size)
        }

    }

    fun getItems(): MutableList<NotesParam.SubNote> {
        return list
    }

}


