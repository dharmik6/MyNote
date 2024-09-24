package com.mynote.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mynote.data.param.NotesParam

class NoteViewModel : ViewModel() {
    val items: MutableLiveData<MutableList<NotesParam.Item>> = MutableLiveData(mutableListOf())

    fun updateItems(newItems: MutableList<NotesParam.Item>) {
        items.value = newItems
    }

    fun getItems(): MutableList<NotesParam.Item> {
        return items.value ?: mutableListOf()
    }


    val SubItems: MutableLiveData<MutableList<NotesParam.SubNote>> = MutableLiveData(mutableListOf())

    fun updateSubItems(newItems: MutableList<NotesParam.SubNote>) {
        SubItems.value = newItems
    }

    fun getSubItems(): MutableList<NotesParam.SubNote> {
        return SubItems.value ?: mutableListOf()
    }
}