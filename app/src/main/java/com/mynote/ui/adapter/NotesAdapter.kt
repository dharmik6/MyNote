package com.mynote.ui.adapter

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.mynote.R
import com.mynote.data.param.NotesParam
import com.mynote.databinding.NotesItem1Binding
import com.mynote.databinding.NotesItem2Binding
import com.mynote.databinding.NotesItem3Binding
import com.mynote.databinding.NotesItemBinding
import com.mynote.interfaces.LongClickListener
import com.mynote.ui.activity.WriteNoteActivity
import com.project.app.utils.AppConstant
import com.project.app.utils.extension.getColor
import com.project.app.utils.extension.launch
import com.project.app.utils.extension.showToast

class NotesAdapter(
    private val context: Context,
    private val list: MutableList<NotesParam>,
    private val longClickListener: LongClickListener
) : RecyclerView.Adapter<ViewHolder>() {

    var isMenuOpen = false

    val TAG = "NotesAdapter"

    class ViewModel(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val binding = when (viewType) {

            1 -> {
                NotesItem1Binding.inflate(LayoutInflater.from(context), parent, false)
            }

            2 -> {
                NotesItem2Binding.inflate(LayoutInflater.from(context), parent, false)
            }

            3 -> {
                NotesItem3Binding.inflate(LayoutInflater.from(context), parent, false)
            }

            else -> {
                NotesItemBinding.inflate(LayoutInflater.from(context), parent, false)
            }
        }
        return ViewModel(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnLongClickListener {
            isMenuOpen = true
            longClickListener.onItemLongClicked(holder.adapterPosition,holder);
            true
        };

        when (holder) {
            is ViewModel -> {
                when (holder.binding) {
                    is NotesItemBinding -> {
                        setDataItem(holder.binding, list[position])
                        if (list[position].isSelected){
                            holder.binding.main.setStrokeColor(getColor(context,R.color.primary_dark))
                        }else{
                            holder.binding.main.setStrokeColor(getColor(context,R.color.base_grey))
                            holder.binding.main.strokeWidth = 1
                        }

                    }

                    is NotesItem1Binding -> {
                        setData1Item(holder.binding, list[position])
                        if (list[position].isSelected){
                            holder.binding.main.setStrokeColor(getColor(context,R.color.primary_dark))
                        }else{
                            holder.binding.main.setStrokeColor(getColor(context,R.color.base_grey))
                            holder.binding.main.strokeWidth = 1
                        }

                        holder.binding.clickableView.setOnClickListener {
                            holder.itemView.performClick()
                        }
                        holder.binding.clickableView.setOnLongClickListener {
                            holder.itemView.performLongClick()
                        }
                    }

                    is NotesItem2Binding -> {
                        setData2Item(holder.binding, list[position])
                        if (list[position].isSelected){
                            holder.binding.main.setStrokeColor(getColor(context,R.color.primary_dark))
                        }else{
                            holder.binding.main.setStrokeColor(getColor(context,R.color.base_grey))
                            holder.binding.main.strokeWidth = 1
                        }

                        holder.binding.rvMiniGoalList.setClickable(false);
                        holder.binding.rvMiniGoalList.setEnabled(false);

                        holder.binding.clickableView.setOnClickListener {
                            holder.itemView.performClick()
                        }
                        holder.binding.clickableView.setOnLongClickListener {
                            holder.itemView.performLongClick()
                        }
                    }

                    is NotesItem3Binding -> {
                        setData3Item(holder.binding, list[position])
                        if (list[position].isSelected){
                            holder.binding.main.setStrokeColor(getColor(context,R.color.primary_dark))
                        }else{
                            holder.binding.main.setStrokeColor(getColor(context,R.color.base_grey))
                            holder.binding.main.strokeWidth = 1
                        }

                        holder.binding.rvMinCheck.setClickable(false);
                        holder.binding.rvMinCheck.setEnabled(false);

                        holder.binding.clickableView.setOnClickListener {
                            holder.itemView.performClick()
                        }
                        holder.binding.clickableView.setOnLongClickListener {
                            holder.itemView.performLongClick()
                        }
                    }
                }
            }
        }

        holder.itemView.setOnClickListener {
            if (!isMenuOpen){
                val type = list[position].type
                context.launch<WriteNoteActivity> {
                    putExtra(AppConstant.NOTE_TYPE, type!!)
                    putExtra(AppConstant.NOTE_DATA, list[position])
                    putExtra(AppConstant.NOTE_ID, list[position].noteId)
                }
                Log.d(TAG, "onBindViewHolder: note data = ${list[position]}")
            }
            else{
                setSelectItem(position,holder)
            }
        }

    }

    fun setSelectItem(position: Int,holder: ViewHolder) {
            if(list[position].isSelected)
                list[position].isSelected = false
            else{
                list[position].isSelected =true
            }
            when (holder) {
                is ViewModel -> {
                    when (holder.binding) {
                        is NotesItemBinding -> {
                            if (list[position].isSelected){
                                holder.binding.main.setStrokeColor(getColor(context,R.color.primary_dark))
                                holder.binding.main.strokeWidth = 5
                            }else{
                                holder.binding.main.setStrokeColor(getColor(context,R.color.base_grey))
                                holder.binding.main.strokeWidth = 1
                            }
                        }

                        is NotesItem1Binding -> {
                            if (list[position].isSelected){
                                holder.binding.main.setStrokeColor(getColor(context,R.color.primary_dark))
                                holder.binding.main.strokeWidth = 5
                            }else{
                                holder.binding.main.setStrokeColor(getColor(context,R.color.base_grey))
                                holder.binding.main.strokeWidth = 1
                            }
                        }

                        is NotesItem2Binding -> {
                            if (list[position].isSelected){
                                holder.binding.main.setStrokeColor(getColor(context,R.color.primary_dark))
                                holder.binding.main.strokeWidth = 5
                            }else{
                                holder.binding.main.setStrokeColor(getColor(context,R.color.base_grey))
                                holder.binding.main.strokeWidth = 1
                            }
                        }

                        is NotesItem3Binding -> {
                            if (list[position].isSelected){
                                holder.binding.main.setStrokeColor(getColor(context,R.color.primary_dark))
                                holder.binding.main.strokeWidth = 5
                            }else{
                                holder.binding.main.setStrokeColor(getColor(context,R.color.base_grey))
                                holder.binding.main.strokeWidth = 1
                            }
                        }
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
    private fun setAnimation(viewToAnimate: View) {
        val context = viewToAnimate.context
        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
        viewToAnimate.startAnimation(animation)
    }

    private fun setData1Item(binding: NotesItem1Binding, item: NotesParam) {
        binding.listTitle.text = item.title
        if (!item.color.isNullOrBlank()) {
            binding.main.setCardBackgroundColor(Color.parseColor(item.color))
        }

        val nonNullItems = item.items?.filterNotNull() ?: emptyList()
        binding.rvMiniList.layoutManager = LinearLayoutManager(context)
        val adapter = MiniListAdapter(context, nonNullItems)
        binding.rvMiniList.adapter = adapter
    }

    private fun setData2Item(binding: NotesItem2Binding, item: NotesParam) {
        binding.goalTitle.text = item.title
        if (!item.color.isNullOrBlank()) {
            binding.main.setCardBackgroundColor(Color.parseColor(item.color))
        }

        val nonNullItems = item.items?.filterNotNull() ?: emptyList()
        binding.rvMiniGoalList.layoutManager = LinearLayoutManager(context)
        val adapter = MiniGoalAdapter(context, nonNullItems)
        binding.rvMiniGoalList.adapter = adapter
    }

    private fun setData3Item(binding: NotesItem3Binding, item: NotesParam) {
        binding.taskTitle.text = item.title
        if (!item.color.isNullOrBlank()) {
            binding.main.setCardBackgroundColor(Color.parseColor(item.color))
        }

        val checkedItems =
            item.subNotes?.filterNotNull()?.filter { it.isChecked == false } ?: emptyList()

        binding.rvMinCheck.layoutManager = LinearLayoutManager(context)
        val adapter = MiniSubNoteCheckedAdapter(context, checkedItems)
        binding.rvMinCheck.adapter = adapter

        val unCheckedItems =
            item.subNotes?.filterNotNull()?.filter { it.isChecked == true } ?: emptyList()
        binding.rvMinUncheck.layoutManager = LinearLayoutManager(context)
        val uncheckedAdapter = MiniSubNoteUnCheckedAdapter(context, unCheckedItems)
        binding.rvMinUncheck.adapter = uncheckedAdapter
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int): Int {
        return when (list[position].type) {
            1 -> 1
            2 -> 2
            3 -> 3
            else -> 0
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

}
