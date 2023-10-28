package com.example.firestoreapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firestoreapp.databinding.ActivityMainBinding
import com.example.firestoreapp.domain.entity.Note
import com.example.firestoreapp.ui.adapter.ItemTouchHelperCallback
import com.example.firestoreapp.ui.adapter.MainAdapter
import dagger.android.AndroidInjection
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModel: MainViewModel


    lateinit var itemTouchHelper: ItemTouchHelper//  для свайпов и т.п.
    private val mainAdapter by lazy {
        MainAdapter(
            onEditNoteClicked = viewModel::onEditNoteClicked,
            onDeleteNoteClicked = viewModel::onDeleteNoteClicked
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AndroidInjection.inject(this)

        viewModel.loadData()
        initUi()
        observeViewModelData()
        observeLoadingVisible()
        observeEditNote()
    }

    private fun initUi() {
        with(binding.rvNotes) {
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }

        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(mainAdapter))
        itemTouchHelper.attachToRecyclerView(binding.rvNotes)

        binding.floatingActionButton.setOnClickListener {
            showAddNoteDialog(null)
        }
    }

    private fun observeViewModelData() {
        this@MainActivity.lifecycleScope.launch {
            this@MainActivity.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultRecycler.collect {
                    mainAdapter.setData(it)
                }
            }
        }
    }

    private fun observeLoadingVisible() {
        this@MainActivity.lifecycleScope.launch {
            this@MainActivity.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    binding.loadingFrameLayout.root.isVisible = it
                    binding.progressCircular.isVisible = it
                }
            }
        }
    }

    private fun observeEditNote() {
        this@MainActivity.lifecycleScope.launch {
            this@MainActivity.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.noteIdFlow
                    .collect {
                        showAddNoteDialog(it)
                    }
            }
        }
    }

    private fun showAddNoteDialog(note: Note?) {
        NoteDialogFragment.newInstance(note)
            .show(
                supportFragmentManager,
                ADD_NOTE_DIALOG_TAG
            )
    }

    companion object {
        const val ADD_NOTE_DIALOG_TAG = "ADD_NOTE_DIALOG_FRAGMENT"
    }
}