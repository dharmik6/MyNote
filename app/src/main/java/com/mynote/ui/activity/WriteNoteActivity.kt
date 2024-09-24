package com.mynote.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.mynote.R
import com.mynote.data.param.NotesParam
import com.mynote.databinding.ActivityWriteNoteBinding
import com.mynote.interfaces.OnColorSelectListener
import com.mynote.interfaces.OnNoteDeleteListener
import com.mynote.interfaces.OnSubNoteCheckedChangeListener
import com.mynote.interfaces.OnSubNoteUnCheckedChangeListener
import com.mynote.ui.adapter.BuyingItemAdapter
import com.mynote.ui.adapter.GoalItemAdapter
import com.mynote.ui.adapter.SubNoteCheckAdapter
import com.mynote.ui.adapter.SubNoteUnCheckAdapter
import com.mynote.ui.dialog.NoteMenuDialog
import com.mynote.viewmodel.FirebaseUserViewModel
import com.mynote.viewmodel.NoteViewModel
import com.project.app.base.BaseActivity
import com.project.app.utils.AppConstant
import com.project.app.utils.PrefUtils
import com.project.app.utils.extension.getCurrentTimeZone
import com.project.app.utils.extension.loadWithGlide
import com.project.app.utils.extension.luciferLog
import com.project.app.utils.extension.viewBinding
import com.project.app.utils.resource.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.log


@AndroidEntryPoint
class WriteNoteActivity : BaseActivity(), OnClickListener, OnColorSelectListener,
    OnNoteDeleteListener, OnSubNoteCheckedChangeListener,
    OnSubNoteUnCheckedChangeListener {

    @Inject
    lateinit var prefUtils: PrefUtils
    private val mViewModel: FirebaseUserViewModel by viewModels()
    private val noteViewModel: NoteViewModel by viewModels()
    private val binding by viewBinding { ActivityWriteNoteBinding.inflate(layoutInflater) }
    private var noteType = -1
    private val TAG = "WriteNoteActivity"
    lateinit var userId: String
    private var noteId: String? = null

    private var noteData: NotesParam? = null


    // adapters
    lateinit var itemAdapter: BuyingItemAdapter
    lateinit var goalItemAdapter: GoalItemAdapter
    private lateinit var checkAdapter: SubNoteCheckAdapter
    private lateinit var unCheckAdapter: SubNoteUnCheckAdapter

    var currentTime: String = ""
    var bgColor: String = "#FAF8FC"
    var archived = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setRecyclerView()
        noteData = intent.getParcelableExtra(AppConstant.NOTE_DATA)
        noteId = intent.getStringExtra(AppConstant.NOTE_ID)
        if (noteData != null) {
            setNoteData(noteData!!)
            Log.d(TAG, "onCreate: set the data = $noteData")
        }

        currentTime = getCurrentTimeZone()


        userId = prefUtils.getAuthToken()!!

        binding.editedTime.text = "Edited $currentTime"
        noteType = intent.getIntExtra(AppConstant.NOTE_TYPE, -1)
        if (noteType != -1) {
            loadPage()
        } else {
            onBackPressedDispatcher
        }

        val dialog = NoteMenuDialog()
        dialog.setOnNoteDeleteListener(this)
        observer()
        setListener()
    }

    private fun setNoteData(data: NotesParam) {
        Log.d(TAG, "setNoteData: note type = ${data.type}")
        bgColor = data.color!!
        binding.main.setBackgroundColor(Color.parseColor(bgColor))

        archived = data.isArchived
        Log.d(TAG, "setNoteData: data archive = ${data.isArchived}")

        when (data.type) {
            0 -> {
                binding.title.setText(data.title)
                binding.note.setText(data.noteText)
            }

            1 -> {
                binding.listTitle.setText(data.title)
                val nonNullItems = data.items?.filterNotNull() ?: emptyList()
                itemAdapter.addItems(nonNullItems)
                Log.d(TAG, "setNoteData: title = ${data.title} items list = ${data.items}")
            }

            2 -> {
                binding.goalTitle.setText(data.title)
                val nonNullItems = data.items?.filterNotNull() ?: emptyList()
                goalItemAdapter.addItems(nonNullItems)
                Log.d(TAG, "setNoteData: title = ${data.title} items list = ${data.items}")
            }

            3 -> {
                binding.taskTitle.setText(data.title)
                val unCheckedItems =
                    data.subNotes?.filterNotNull()?.filter { it.isChecked == false } ?: emptyList()
                unCheckAdapter.addItems(unCheckedItems)

                val checkedItems =
                    data.subNotes?.filterNotNull()?.filter { it.isChecked == true } ?: emptyList()
                checkAdapter.addItems(checkedItems)
                Log.d(TAG, "setNoteData: title = ${data.title} sub note list = ${data.subNotes}")
            }
        }
    }

    private fun setListener() {
        binding.menu.setOnClickListener(this)
        binding.back.setOnClickListener(this)
        binding.search.setOnClickListener(this)
        binding.addGoalItem.setOnClickListener(this)
        binding.addNote.setOnClickListener(this)
        binding.save.setOnClickListener(this)
        binding.addItem.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.menu -> {
                val dialog = NoteMenuDialog()
                dialog.setOnColorSelectListener(this)
                dialog.show(supportFragmentManager, "MyDialogTag")

            }

            binding.back -> {
                onBackPressedDispatcher.onBackPressed()
            }

            binding.save -> {

                if (!noteId.isNullOrBlank()) {
                    Log.d(TAG, "onClick: update note $noteId")
                    updateNote()
                } else {
                    Log.d(TAG, "onClick: save note")
                    sendNote()
                }

            }

            binding.addItem -> {
                addItem()
            }

            binding.addGoalItem -> {
                addGoalItem()
            }

            binding.addNote -> {
                unCheckAdapter.addItem(NotesParam.SubNote())
            }
        }
    }


    private fun loadPage() {
        when (noteType) {
            0 -> {
                binding.viewNoteAnimator.displayedChild = 0
                binding.addNote.isInvisible = true
            }

            1 -> {
                binding.viewNoteAnimator.displayedChild = 1
                binding.addNote.isInvisible = true
            }

            2 -> {
                binding.viewNoteAnimator.displayedChild = 2
                binding.addNote.isInvisible = true
            }

            3 -> {
                binding.viewNoteAnimator.displayedChild = 3
                binding.addNote.isInvisible = false
            }
        }
    }

    private fun setRecyclerView() {
        // Initialize RecyclerView with the ViewModel's list
        binding.rvBuyingItem.layoutManager = LinearLayoutManager(this)
        itemAdapter = BuyingItemAdapter(this, noteViewModel.getItems())
        binding.rvBuyingItem.adapter = itemAdapter


        // goal recyclerview
        binding.rvGoalItem.layoutManager = LinearLayoutManager(this)
        goalItemAdapter = GoalItemAdapter(this, noteViewModel.getItems())
        binding.rvGoalItem.adapter = goalItemAdapter

        // Setup RecyclerViews for both adapters
        binding.rvCheckedItems.layoutManager = LinearLayoutManager(this)
        unCheckAdapter = SubNoteUnCheckAdapter(this, mutableListOf(), this)
        binding.rvCheckedItems.adapter = unCheckAdapter

        binding.rvUncheckedItems.layoutManager = LinearLayoutManager(this)
        checkAdapter = SubNoteCheckAdapter(this, noteViewModel.getSubItems(), this)
        binding.rvUncheckedItems.adapter = checkAdapter


    }

    private fun addItem() {
        luciferLog("add item clicked")
        val newItem = NotesParam.Item(isChecked = false, name = "")
        itemAdapter.addItem(newItem)
    }

    private fun addGoalItem() {
        val newItem = NotesParam.Item(isChecked = false, name = "")
        goalItemAdapter.addItem(newItem)
    }

    override fun onColorSelectListener(color: String) {
        bgColor = color
        binding.main.setBackgroundColor(Color.parseColor(color))
        Log.d(TAG, "onColorSelectListener: color = $color")
    }

    override fun onNoteDeleteListener(delete: Int) {
        if (delete == 1) {
            Log.d(TAG, "onNoteDeleteListener: note deleted")
        }
    }

    private fun NotesParamModle(): NotesParam {
        var notesParam: NotesParam? = null
        val createdAt = getCurrentTimeZone()
        val updatedAt = getCurrentTimeZone()
        val archived = archived

        when (noteType) {
            0 -> {
                val title = binding.title.text.toString()
                val note = binding.note.text.toString()


                notesParam = NotesParam(
                    noteText = note,
                    title = title,
                    type = noteType,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    color = bgColor,
                    isArchived = archived
                )
                Log.d(TAG, "NotesParamModle: color = $bgColor")
            }

            1 -> {
                val updatedList = itemAdapter.getItems()

                val title = binding.listTitle.text.toString()
                notesParam = NotesParam(
                    title = title,
                    type = noteType,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    items = updatedList,
                    color = bgColor,
                    isArchived = archived
                )
            }

            2 -> {
                val updatedList = goalItemAdapter.getItems()
                val title = binding.goalTitle.text.toString()
                notesParam = NotesParam(
                    title = title,
                    type = noteType,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    items = updatedList,
                    color = bgColor,
                    isArchived = archived
                )
            }

            3 -> {
                var subNoteList = unCheckAdapter.getItems()
                subNoteList.addAll(checkAdapter.getItems())
                val title = binding.taskTitle.text.toString()
                notesParam = NotesParam(
                    title = title,
                    type = noteType,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    subNotes = subNoteList,
                    color = bgColor,
                    isArchived = archived
                )
            }
        }

        return notesParam!!
    }

    private fun sendNote() {
        Log.d(TAG, "sendNote: note are save ")
        if (userId != null)
            mViewModel.sendNoteData(userId, NotesParamModle())
    }

    private fun updateNote() {
        Log.d(TAG, "sendNote: note are update ")
        if (userId != null)
            mViewModel.updateNoteData(
                userId = userId,
                noteId = noteId!!,
                notesParam = NotesParamModle()
            )
    }

    private fun observer() {
        mViewModel.sendNoteLiveData.observe(this) { state ->
            when (state.status) {
                Status.SUCCESS -> {
                    hideProgress()
                    onBackPressedDispatcher.onBackPressed()
                }

                Status.ERROR -> {
                    hideProgress()
                    Log.e(TAG, "observer: ${state.error.toString()}")
                }

                Status.LOADING -> {
                    showProgress()
                }
            }

        }
        mViewModel.updateNoteLiveData.observe(this) { state ->
            when (state.status) {
                Status.SUCCESS -> {
                    hideProgress()
                    onBackPressedDispatcher.onBackPressed()
                }

                Status.ERROR -> {
                    hideProgress()
                    Log.e(TAG, "observer: ${state.error.toString()}")
                }

                Status.LOADING -> {
                    showProgress()
                }
            }

        }
    }

    override fun onSubNoteCheckedChangeListener(data: NotesParam.SubNote) {
        Log.d(TAG, "onSubNoteCheckedChangeListener: sub note data = $data")
        checkAdapter.addItem(data)
    }

    override fun onSubNoteUnCheckedChangeListener(data: NotesParam.SubNote) {
        Log.d(TAG, "onSubNoteCheckedChangeListener: sub note data = $data")
        unCheckAdapter.addItem(data)
    }


}