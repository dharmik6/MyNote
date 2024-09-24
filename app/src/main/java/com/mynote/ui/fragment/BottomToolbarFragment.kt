package com.mynote.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.mynote.R
import com.mynote.databinding.FragmentBottomToolbarBinding
import com.mynote.ui.activity.CreateNoteActivity
import com.mynote.ui.activity.WriteNoteActivity
import com.project.app.base.BaseFragment
import com.project.app.utils.AppConstant
import com.project.app.utils.extension.launch
import com.project.app.utils.extension.viewBinding

class BottomToolbarFragment : BaseFragment() ,OnClickListener {
    private val binding by viewBinding { FragmentBottomToolbarBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setListener()
        return binding.root
    }

    private fun setListener(){
        binding.createNote.setOnClickListener(this)
        binding.note1.setOnClickListener(this)
        binding.note2.setOnClickListener(this)
        binding.note3.setOnClickListener(this)
        binding.note4.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.createNote -> {
                launch<CreateNoteActivity>()
            }
            binding.note1 -> { launch<WriteNoteActivity> { putExtra(AppConstant.NOTE_TYPE,0) }}
            binding.note2 -> { launch<WriteNoteActivity> { putExtra(AppConstant.NOTE_TYPE,1) }}
            binding.note3 -> { launch<WriteNoteActivity> { putExtra(AppConstant.NOTE_TYPE,2) }}
            binding.note4 -> { launch<WriteNoteActivity> { putExtra(AppConstant.NOTE_TYPE,3) }}
        }
    }
}