package com.mynote.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mynote.data.param.NotesParam
import com.mynote.databinding.MiniGoalsItemBinding

class MiniGoalAdapter(private val context: Context, private val list : List<NotesParam.Item>) : RecyclerView.Adapter<MiniGoalAdapter.ViewHolder>() {
    class ViewHolder(val binding : MiniGoalsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : NotesParam.Item){
            binding.item.text = item.name

            if (item.isChecked != null)
                binding.checkboxItem.isChecked = item.isChecked!!


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MiniGoalsItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return  ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])

        val nonNullItems = list[position].subItems?.filterNotNull() ?: emptyList()
        holder.binding.rvSubItem.layoutManager = LinearLayoutManager(context)
        val adapter = MiniSubItemAdapter(context,nonNullItems)
        holder.binding.rvSubItem.adapter = adapter
    }
}