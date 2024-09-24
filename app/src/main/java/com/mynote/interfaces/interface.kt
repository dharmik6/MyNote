package com.mynote.interfaces

import androidx.recyclerview.widget.RecyclerView
import com.mynote.data.param.NotesParam


interface LongClickListener {
    fun onItemLongClicked(position: Int,holder: RecyclerView.ViewHolder)
}

interface OnLongClickListener {
    fun onLongClickListener(position: Int)
}

interface OnSubNoteUnCheckedChangeListener{
    fun onSubNoteUnCheckedChangeListener(data : NotesParam.SubNote)
}

interface OnNoteDeleteListener {
    fun onNoteDeleteListener(delete: Int)
}

interface OnColorSelectListener {
    fun onColorSelectListener(color: String)
}

interface OnSubNoteCheckedChangeListener {
    fun onSubNoteCheckedChangeListener(data: NotesParam.SubNote)
}