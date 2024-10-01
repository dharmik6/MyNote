package com.mynote.ui.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mynote.R
import com.mynote.data.param.NotesParam
import com.mynote.databinding.NotesItem1Binding
import com.mynote.databinding.NotesItem3Binding
import com.mynote.interfaces.OnLongClickListener
import com.mynote.ui.activity.WriteNoteActivity
import com.mynote.ui.adapter.GoalAdapter.ViewHolder
import com.project.app.utils.AppConstant
import com.project.app.utils.extension.getColor
import com.project.app.utils.extension.launch

class TaskAdapter(private val context: Context, private val list: MutableList<NotesParam>,private val listener: OnLongClickListener) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private  val TAG = "TaskAdapter"
    var isMenuOpen = false
    class ViewHolder(val binding: NotesItem3Binding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NotesItem3Binding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setData3Item(holder.binding,list[position])

        if (list[position].isSelected){
            holder.binding.main.setStrokeColor(getColor(context,R.color.primary_dark))
            holder.binding.main.strokeWidth = 5
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

        holder.binding.clickableView.setOnClickListener {
            holder.itemView.performClick()
        }
        holder.binding.clickableView.setOnLongClickListener {
            holder.itemView.performLongClick()
        }

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



    private fun setData3Item(binding: NotesItem3Binding, item: NotesParam) {
        binding.taskTitle.text = item.title
        if (!item.color.isNullOrBlank()) {
            binding.main.setCardBackgroundColor(Color.parseColor(item.color))
        }

        val checkedItems = item.subNotes?.filterNotNull()?.filter { it.isChecked == false } ?: emptyList()

        binding.rvMinCheck.layoutManager = LinearLayoutManager(context)
        val adapter = MiniSubNoteCheckedAdapter(context, checkedItems)
        binding.rvMinCheck.adapter = adapter

        val unCheckedItems = item.subNotes?.filterNotNull()?.filter { it.isChecked == true } ?: emptyList()
        binding.rvMinUncheck.layoutManager = LinearLayoutManager(context)
        val uncheckedAdapter = MiniSubNoteUnCheckedAdapter(context, unCheckedItems)
        binding.rvMinUncheck.adapter = uncheckedAdapter

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