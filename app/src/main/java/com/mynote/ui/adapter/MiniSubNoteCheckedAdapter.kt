package com.mynote.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mynote.data.param.NotesParam
import com.mynote.databinding.MiniNotesSubItemBinding

class MiniSubNoteCheckedAdapter(private val context: Context, private val list : List<NotesParam.SubNote>) : RecyclerView.Adapter<MiniSubNoteCheckedAdapter.ViewHolder>() {
    class ViewHolder(val binding : MiniNotesSubItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : NotesParam.SubNote){
            binding.title.text = item.title
                binding.noteCheck.isChecked = item.isChecked
            binding.mainCard.setCardBackgroundColor(Color.parseColor(item.color))

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MiniNotesSubItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return  ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])

    }
}