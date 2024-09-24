package com.mynote.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mynote.R
import com.mynote.databinding.ExtraNotesMenuDialogBinding
import com.mynote.interfaces.OnColorSelectListener
import com.mynote.interfaces.OnNoteDeleteListener
import com.mynote.ui.activity.WriteNoteActivity
import com.project.app.utils.extension.loadWithGlide

class NoteMenuDialog : BottomSheetDialogFragment(), OnClickListener {

    lateinit var binding: ExtraNotesMenuDialogBinding

    private var onColorSelectListener: OnColorSelectListener? = null
    private var onNoteDeleteListener: OnNoteDeleteListener? = null



    private val TAG = "NoteMenuDialog"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExtraNotesMenuDialogBinding.inflate(inflater, container, false)
        colorSelect()
        setListener()

        Log.d(TAG, "onCreateView: archived = ${(activity as WriteNoteActivity).archived}")
        if ((activity as WriteNoteActivity).archived == true) {
            binding.archiveImage.loadWithGlide(R.drawable.folder_download)
        } else {
            binding.archiveImage.loadWithGlide(R.drawable.folder_upload)
        }
        return binding.root
    }

    private fun setListener() {
        binding.dialogClose.setOnClickListener(this)
        binding.noteAchieve.setOnClickListener(this)
        binding.noteDelete.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.dialogClose -> {
                dismiss()
            }

            binding.noteAchieve -> {
                if ((activity as WriteNoteActivity).archived == false) {
                    (activity as WriteNoteActivity).archived = true
                    binding.archiveImage.loadWithGlide(R.drawable.folder_download)
                } else {
                    (activity as WriteNoteActivity).archived = false
                    binding.archiveImage.loadWithGlide(R.drawable.folder_upload)
                }
                Log.d(TAG, "onClick: archived = ${(activity as WriteNoteActivity).archived}")
            }

            binding.noteDelete -> {
                if (onNoteDeleteListener != null) {
                    onNoteDeleteListener!!.onNoteDeleteListener(1)
                }
            }
        }
    }

    fun colorSelect() {
        binding.rgColor.setOnCheckedChangeListener { group, checkedId ->
            if (onColorSelectListener != null) {
                when (checkedId) {

                    R.id.rb_1 -> {
                        onColorSelectListener!!.onColorSelectListener("#FFFFFF")
                    }

                    R.id.rb_2 -> {
                        onColorSelectListener!!.onColorSelectListener("#EFE9F7")
                    }

                    R.id.rb_3 -> {
                        onColorSelectListener!!.onColorSelectListener("#F7DEE3")
                    }

                    R.id.rb_4 -> {
                        onColorSelectListener!!.onColorSelectListener("#DAF6E4")
                    }

                    R.id.rb_5 -> {
                        onColorSelectListener!!.onColorSelectListener("#FDEBAB")
                    }

                    R.id.rb_6 -> {
                        onColorSelectListener!!.onColorSelectListener("#F7F6D4")
                    }

                    R.id.rb_7 -> {
                        onColorSelectListener!!.onColorSelectListener("#EFEEF0")
                    }
                }
            }

        }
    }


    fun setOnColorSelectListener(onColorSelectListener: OnColorSelectListener?) {
        this.onColorSelectListener = onColorSelectListener
    }


    fun setOnNoteDeleteListener(onNoteDeleteListener: OnNoteDeleteListener?) {
        this.onNoteDeleteListener = onNoteDeleteListener
    }


}