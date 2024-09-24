package com.mynote.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mynote.R
import com.mynote.databinding.FragmentBuyingBinding
import com.mynote.databinding.FragmentTasksBinding
import com.mynote.interfaces.OnColorSelectListener
import com.mynote.interfaces.OnLongClickListener
import com.mynote.ui.activity.HomeActivity
import com.mynote.ui.adapter.BuyingAdapter
import com.mynote.ui.adapter.TaskAdapter
import com.mynote.ui.dialog.ColorDialog
import com.mynote.viewmodel.FirebaseUserViewModel
import com.project.app.base.BaseFragment
import com.project.app.utils.PrefUtils
import com.project.app.utils.extension.showToast
import com.project.app.utils.extension.viewBinding
import com.project.app.utils.resource.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TasksFragment : BaseFragment(), OnClickListener, OnLongClickListener, OnColorSelectListener {

    @Inject
    lateinit var prefUtils: PrefUtils
    private val mViewModel: FirebaseUserViewModel by viewModels()
    private val binding by viewBinding { FragmentTasksBinding.inflate(layoutInflater) }
    private val TAG = "IdeaFragment"
    private lateinit var adapter: TaskAdapter
    private var colorDialog: ColorDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        setRecyclerView()
        (activity as HomeActivity).binding.layout.setOnCheckedChangeListener{ btn ,ischecked ->
            if (ischecked){
                binding.rvTask.layoutManager = StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL)
            }else{
                binding.rvTask.layoutManager = LinearLayoutManager(requireContext())
            }
            setRecyclerView()
            getData()
        }
        observer()
        setupSwipeToRefresh()
        setListener()
        getData()
        return binding.root
    }

    private fun setRecyclerView() {
        if ((activity as HomeActivity).binding.layout.isChecked){
            binding.rvTask.layoutManager = StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL)
        }else{
            binding.rvTask.layoutManager = LinearLayoutManager(requireContext())
        }
        adapter = TaskAdapter(requireContext(), mutableListOf(),this)
        binding.rvTask.adapter = adapter
    }

    private fun getData() {
        val userId = prefUtils.getAuthToken()
        if (userId != null)
            mViewModel.getNoteList(userId)
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            adapter.clearData()
            val userId = prefUtils.getAuthToken()
            if (userId != null){
                mViewModel.getNoteList(userId)
            }

            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun observer() {
        mViewModel.getNoteListLiveData.observe(viewLifecycleOwner) { state ->
            when (state.status) {
                Status.SUCCESS -> {
                    binding.viewNoteAnimator.displayedChild = 1
                    var data = state.data
                    if (!data.isNullOrEmpty()) {
                        val filteredData = data.filter { it.type == 3 && !it.isArchived }

                        adapter.addItems(filteredData)

                        Log.d(TAG, "observer: filteredData = $filteredData")
                    }
                    else
                        binding.viewNoteAnimator.displayedChild = 0

                }

                Status.ERROR -> {
                    binding.viewNoteAnimator.displayedChild = 0
                    Log.e(TAG, "observer222: ${state.error?.errorMessage}")
                }

                Status.LOADING -> {
                    binding.viewNoteAnimator.displayedChild = 2
                }
            }
        }
    }

    private fun setListener(){
        (activity as HomeActivity).binding.cancel.setOnClickListener(this)
        (activity as HomeActivity).binding.delete.setOnClickListener(this)
        (activity as HomeActivity).binding.color.setOnClickListener(this)
        (activity as HomeActivity).binding.archive.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            (activity as HomeActivity).binding.cancel -> {
                (activity as? HomeActivity)!!.binding.viewAnimatorToolbar.displayedChild = 0
                adapter.isMenuOpen = false
                adapter.deSelectList()
            }
            (activity as HomeActivity).binding.delete -> {
                val deleteList = adapter.getSelectedItemList()
                adapter.removeSelectedItems()
                val userId = prefUtils.getAuthToken()
                if (userId != null){
                    mViewModel.deleteNotes(userId,deleteList)
                    Log.d(TAG, "onClick: userId = $userId , noteData  = $deleteList")
                }


                (activity as? HomeActivity)!!.binding.viewAnimatorToolbar.displayedChild = 0
                adapter.isMenuOpen = false
                adapter.deSelectList()

            }
            (activity as HomeActivity).binding.color -> {

                colorDialog = ColorDialog()
                colorDialog?.setOnColorSelectListener(this)
                colorDialog?.show(childFragmentManager,colorDialog!!.tag)
            }
            (activity as HomeActivity).binding.archive -> {
                adapter.setArchived()

                val archivedList = adapter.getSelectedItemList()
                adapter.removeSelectedItems()
                val userId = prefUtils.getAuthToken()
                if (userId != null){
                    mViewModel.updateNotes(userId,archivedList)
                    Log.d(TAG, "onClick: userId = $userId , noteData  = $archivedList")
                }
                (activity as? HomeActivity)!!.binding.viewAnimatorToolbar.displayedChild = 0
                adapter.isMenuOpen = false
                adapter.deSelectList()
                showToast("Notes archived")
            }

        }
    }

    override fun onLongClickListener(position: Int) {
        (activity as? HomeActivity)!!.binding.viewAnimatorToolbar.displayedChild = 1
    }

    override fun onColorSelectListener(color: String) {
        adapter.changeBgColor(color)

        val notesData = adapter.getSelectedItemList()
        val userId = prefUtils.getAuthToken()
        if (userId != null){
            mViewModel.updateNotes(userId,notesData)
            Log.d(TAG, "onClick: userId = $userId , noteData  = $notesData")
        }

        colorDialog?.dismiss()
        colorDialog = null

        (activity as? HomeActivity)!!.binding.viewAnimatorToolbar.displayedChild = 0
        adapter.isMenuOpen = false
        adapter.deSelectList()

    }
}