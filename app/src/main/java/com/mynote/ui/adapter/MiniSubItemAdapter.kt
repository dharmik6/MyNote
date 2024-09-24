package com.mynote.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mynote.data.param.NotesParam
import com.mynote.databinding.MiniBuyingItemBinding

class MiniSubItemAdapter (private val context: Context, private val list : List<NotesParam.Item.SubItem>) : RecyclerView.Adapter<MiniSubItemAdapter.ViewHolder>() {
    class ViewHolder(val binding : MiniBuyingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : NotesParam.Item.SubItem){
            binding.item.text = item.name

            if (item.isChecked != null)
                binding.checkboxItem.isChecked = item.isChecked!!
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MiniBuyingItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return  ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}