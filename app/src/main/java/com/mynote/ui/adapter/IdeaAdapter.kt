package com.mynote.ui.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mynote.R
import com.mynote.data.param.NotesParam
import com.mynote.databinding.NotesItemBinding
import com.mynote.interfaces.LongClickListener
import com.mynote.interfaces.OnLongClickListener
import com.mynote.ui.activity.WriteNoteActivity
import com.project.app.utils.AppConstant
import com.project.app.utils.extension.getColor
import com.project.app.utils.extension.launch

class IdeaAdapter(
    private val context: Context,
    private val list: MutableList<NotesParam>,
    private val listener: OnLongClickListener
) : RecyclerView.Adapter<IdeaAdapter.ViewHolder>() {

    private val TAG = "IdeaAdapter"
    var isMenuOpen = false
    lateinit var binding: NotesItemBinding

    class ViewHolder(val binding: NotesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotesParam) {
            binding.noteNote.text = item.noteText
            binding.noteTitle.text = item.title
            if (!item.color.isNullOrBlank()) {
                binding.main.setCardBackgroundColor(Color.parseColor(item.color))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = NotesItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setDataItem(holder.binding, list[position])
        if (list[position].isSelected){
            holder.binding.main.setStrokeColor(getColor(context,R.color.primary_dark))
        }else{
            holder.binding.main.setStrokeColor(getColor(context,R.color.base_grey))
            holder.binding.main.strokeWidth = 1
        }

        holder.itemView.setOnLongClickListener {
            isMenuOpen = true
            listener.onLongClickListener(position)
            setSelectedItem(position,holder)
            true
        };


        holder.itemView.setOnClickListener {
            if (isMenuOpen){
                setSelectedItem(position,holder)
            }else {
                val type = list[position].type
                context.launch<WriteNoteActivity> {
                    putExtra(AppConstant.NOTE_TYPE, type!!)
                    putExtra(AppConstant.NOTE_DATA, list[position])
                    putExtra(AppConstant.NOTE_ID, list[position].noteId)
                }
            }

        }
    }

    private fun setDataItem(binding: NotesItemBinding, item: NotesParam) {
        binding.noteNote.text = item.noteText
        binding.noteTitle.text = item.title
        if (!item.color.isNullOrBlank()) {
            binding.main.setCardBackgroundColor(Color.parseColor(item.color))
        }
    }

    fun addItems(newItems: List<NotesParam>) {
        val startPosition = list.size
        list.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    fun clearData() {
        list.clear()
        notifyDataSetChanged()
    }

    // filter
    fun removeSelectedItems() {
        val indicesToRemove = mutableListOf<Int>()

        for (i in list.indices) {
            if (list[i].isSelected) {
                indicesToRemove.add(i)
            }
        }

        for (index in indicesToRemove.reversed()) {
            list.removeAt(index)
            notifyItemRemoved(index)
            Log.d(TAG, "removeSelectedItems: removed item id = $index")
        }
    }

    fun changeBgColor(color : String){
        for (i in list.indices) {
            if (list[i].isSelected) {
                list[i].color = color
                notifyItemChanged(i)
            }
        }
    }

    fun setArchived(){
        for (i in list.indices) {
            if (list[i].isSelected) {
                list[i].isArchived = true
                notifyItemChanged(i)
            }
        }
    }

    fun deSelectList(){
        for (i in list.indices) {
            list[i].isSelected = false
            notifyItemChanged(i)
        }
    }

    fun getSelectedItemList() : List<NotesParam>{
        val selectedList   = list.filter { it.isSelected == true }
        return selectedList
    }

    fun setSelectedItem(position: Int, holder: ViewHolder) {
        val item = list[position]
        Log.d(TAG, "setSelectedItem: is selected = ${item.isSelected}")

        if(list[position].isSelected){
            list[position].isSelected =false
            holder.binding.main.setStrokeColor(context.getColor(R.color.dark_grey))
            holder.binding.main.strokeWidth = 1
            Log.d(TAG, "setSelectedItem: deSelected")
        }
        else{
            list[position].isSelected = true
            holder.binding.main.setStrokeColor(context.getColor(R.color.primary_dark))
            holder.binding.main.strokeWidth = 5
            Log.d(TAG, "setSelectedItem: selected")
        }

    }

}