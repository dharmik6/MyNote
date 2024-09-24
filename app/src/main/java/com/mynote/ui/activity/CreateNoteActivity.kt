package com.mynote.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mynote.R
import com.mynote.databinding.ActivityCreateNoteBinding
import com.project.app.base.BaseActivity
import com.project.app.utils.AppConstant
import com.project.app.utils.extension.launch
import com.project.app.utils.extension.showToast
import com.project.app.utils.extension.viewBinding

class CreateNoteActivity : BaseActivity() , OnClickListener{
    private val binding by viewBinding { ActivityCreateNoteBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setListener()
    }

    private fun setListener(){
        binding.card1.setOnClickListener(this)
        binding.card2.setOnClickListener(this)
        binding.card3.setOnClickListener(this)
        binding.card4.setOnClickListener(this)
        binding.back.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v){
            binding.card1 -> { launch<WriteNoteActivity> { putExtra(AppConstant.NOTE_TYPE,0) }
                finish()}
            binding.card2 -> { launch<WriteNoteActivity> { putExtra(AppConstant.NOTE_TYPE,1) }
                finish()}
            binding.card3 -> { launch<WriteNoteActivity> { putExtra(AppConstant.NOTE_TYPE,2) }
                finish()}
            binding.card4 -> { launch<WriteNoteActivity> { putExtra(AppConstant.NOTE_TYPE,3) }
                finish()}
            binding.back -> { onBackPressedDispatcher.onBackPressed()}
        }
    }
}