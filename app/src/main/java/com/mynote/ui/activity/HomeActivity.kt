package com.mynote.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.mynote.R
import com.mynote.databinding.ActivityHomeBinding
import com.mynote.ui.fragment.ArchiveFragment
import com.mynote.ui.fragment.BuyingFragment
import com.mynote.ui.fragment.GoalsFragment
import com.mynote.ui.fragment.HomeFragment
import com.mynote.ui.fragment.IdeaFragment
import com.mynote.ui.fragment.TasksFragment
import com.project.app.base.BaseActivity
import com.project.app.utils.extension.launch
import com.project.app.utils.extension.loadWithGlide
import com.project.app.utils.extension.showToast
import com.project.app.utils.extension.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity(), OnClickListener, OnNavigationItemSelectedListener {
    val binding by viewBinding { ActivityHomeBinding.inflate(layoutInflater) }
    private val TAG = "HomeActivity"

    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        setDrawer(
            ActionBarDrawerToggle(
                this,
                binding.drawer,
                R.string.nav_open,
                R.string.nav_close
            )
        )
        setListener()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawer.isDrawerOpen(binding.navigationView)) {
                    binding.drawer.closeDrawer(binding.navigationView)
                } else {
                    finish()
                }

            }
        })
        binding.navigationView.setCheckedItem(R.id.side_menu_notes)
        loadFragment(HomeFragment(), true)

        onBackPress()

    }

    private fun setListener() {
        binding.menu.setOnClickListener(this)
        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.menu -> {
                if (binding.drawer.isDrawerOpen(binding.navigationView))
                    binding.drawer.closeDrawer(binding.navigationView)
                else
                    binding.drawer.openDrawer(binding.navigationView)
            }

        }
    }


    private fun setDrawer(drawerToggle: ActionBarDrawerToggle) {
        binding.drawer.addDrawerListener(drawerToggle)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.side_menu_notes -> {
                binding.archive.loadWithGlide(R.drawable.folder_upload)
                loadFragment(HomeFragment(), true)
                Log.d(TAG, "onNavigationItemSelected: notes selected")
                binding.title.text = getString(R.string.notes)
                binding.drawer.closeDrawer(binding.navigationView)
                return true
            }

            R.id.side_menu_achive -> {
                binding.archive.loadWithGlide(R.drawable.folder_download)
                loadFragment(ArchiveFragment(), true)
                Log.d(TAG, "onNavigationItemSelected: achieve selected")
                binding.title.text = getString(R.string.archive)
                binding.drawer.closeDrawer(binding.navigationView)
                return true
            }

            R.id.side_menu_settings -> {
                launch<SettingsActivity>()
                Log.d(TAG, "onNavigationItemSelected: settings selected")
                binding.drawer.closeDrawer(binding.navigationView)
                return true
            }

            R.id.menu_idea_idea -> {
                binding.archive.loadWithGlide(R.drawable.folder_upload)
                loadFragment(IdeaFragment(), true)
                Log.d(TAG, "onNavigationItemSelected: idea selected")
                binding.drawer.closeDrawer(binding.navigationView)
                return true
            }

            R.id.menu_idea_goals -> {
                binding.archive.loadWithGlide(R.drawable.folder_upload)
                loadFragment(GoalsFragment(), true)
                Log.d(TAG, "onNavigationItemSelected: goals selected")
                binding.drawer.closeDrawer(binding.navigationView)
                return true
            }

            R.id.menu_idea_buying -> {
                binding.archive.loadWithGlide(R.drawable.folder_upload)
                loadFragment(BuyingFragment(), true)
                Log.d(TAG, "onNavigationItemSelected: buying selected")
                binding.drawer.closeDrawer(binding.navigationView)
                return true
            }

            R.id.menu_idea_routine -> {
                binding.archive.loadWithGlide(R.drawable.folder_upload)
                loadFragment(TasksFragment(), true)
                Log.d(TAG, "onNavigationItemSelected: routine task selected")
                binding.drawer.closeDrawer(binding.navigationView)
                return true
            }

            else -> {
                return false
            }
        }
    }

    fun loadFragment(fragment: Fragment, flag: Boolean) {
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()

        if (flag)
            fragmentTransaction.replace(R.id.container, fragment)
        else
            fragmentTransaction.add(R.id.container, fragment)

        fragmentTransaction.commit()
    }

    fun onBackPress(){

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {


            }
        })
    }

}