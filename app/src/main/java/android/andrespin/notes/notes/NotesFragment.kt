package android.andrespin.notes.notes

import android.andrespin.notes.BaseFragment
import android.andrespin.notes.R
import android.andrespin.notes.databinding.FragmentNotesBinding
import android.andrespin.notes.model.NoteData
import android.andrespin.notes.model.noteId
import android.andrespin.notes.notes.adapter.NotesAdapter
import android.andrespin.notes.notes.intent.NotesEvent
import android.andrespin.notes.notes.intent.NotesIntent
import android.andrespin.notes.notes.intent.NotesState
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesFragment() : BaseFragment<FragmentNotesBinding, NotesViewModel>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNotesBinding
        get() = FragmentNotesBinding::inflate

    override val viewModelClass: Class<NotesViewModel>
        get() = NotesViewModel::class.java

    private val adapter: NotesAdapter by lazy { NotesAdapter(requireContext(), viewModel, this) }

    override fun setUpViews() {


        initListeners()

        downloadNotes()

        observeViewModel()

        initAdapter()

    }


    private fun initListeners() = with(viewBinding) {
        imgOpenProfile.setOnClickListener {
            findNavController().navigate(R.id.action_notes_to_profile)
        }
        imgAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_notes_to_note)
        }

        imgDelete.setOnClickListener {

            lifecycleScope.launch {
                viewModel.intent.send(NotesIntent.DeleteCheckedNotes)
            }

        }

        imgCancel.setOnClickListener {
            lifecycleScope.launch {
                viewModel.intent.send(NotesIntent.CancelCheckedNotes)
            }
        }

    }

    private fun initAdapter() {
        viewBinding.rvNotes.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.rvNotes.adapter = adapter
    }

    private fun downloadNotes() {
        lifecycleScope.launch {
            viewModel.intent.send(NotesIntent.DownloadNotes)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is NotesState.Notes -> setNotesToAdapter(it.list)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.event.consumeAsFlow().collect {
                when (it) {
                    is NotesEvent.OpenNote -> openNote(it)
                    is NotesEvent.SetNoteListWithNewVars -> setNotesToAdapter(it.list)
                    is NotesEvent.ShowButtons -> showButtons()
                    is NotesEvent.HideButtons -> hideButtons()
                }
            }
        }

    }

    private fun hideButtons() = with(viewBinding) {
        layoutBtnDelete.visibility = View.GONE
    }


    private fun showButtons() = with(viewBinding) {
        layoutBtnDelete.visibility = View.VISIBLE
    }

    private fun openNote(it: NotesEvent.OpenNote) {
        val _id = it.id
        lifecycleScope.launch {
            val bundle = bundleOf(
                noteId to _id
            )
            findNavController().navigate(R.id.action_notes_to_note, bundle)
        }
    }

    private fun setNotesToAdapter(list: List<NoteData>) {
        adapter.setData(list)
    }

}








