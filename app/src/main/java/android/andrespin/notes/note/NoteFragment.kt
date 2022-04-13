package android.andrespin.notes.note

import android.andrespin.notes.BaseFragment
import android.andrespin.notes.R
import android.andrespin.notes.databinding.FragmentNoteBinding
import android.andrespin.notes.model.NoteData
import android.andrespin.notes.model.noteId
import android.andrespin.notes.note.intent.NoteIntent
import android.andrespin.notes.note.intent.NoteState
import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_rv_note_data.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.days

@AndroidEntryPoint
class NoteFragment : BaseFragment<FragmentNoteBinding, NoteViewModel>() {

    private var id: Int? = 0

    private var isNoteEdited = false

    override val viewModelClass: Class<NoteViewModel>
        get() = NoteViewModel::class.java

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNoteBinding
        get() = FragmentNoteBinding::inflate

    @SuppressLint("SimpleDateFormat")
    override fun setUpViews() {

        startTimeAndDateObserving()

        initListeners()

        initBodyOnChangeListener()

        initHeaderOnChangeListener()

        observeViewModel()

        initNoteId()

    }

    private fun initNoteId() {
        id = arguments?.getInt(noteId)
        println("init note id $id")
        lifecycleScope.launch {
            viewModel.intent.send(NoteIntent.GetNoteById(id))
        }
    }

    override fun pause() {
        super.pause()
        lifecycleScope.launch {
            viewModel.intent.send(NoteIntent.SaveNote(id, isNoteEdited))
        }
    }

    private fun startTimeAndDateObserving() = lifecycleScope.launch {
        viewModel.intent.send(NoteIntent.StartTimeAndDateObserving)
    }

    private fun initListeners() =
        with(viewBinding) {
            imgBack.setOnClickListener {
                val navController = findNavController()
                navController.popBackStack()
            }
        }

    private fun initBodyOnChangeListener() =

        viewBinding.editTextBody.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                isNoteEdited = true
                val text = s.toString()
                lifecycleScope.launch {
                    viewModel.intent.send(NoteIntent.SendBody(text))
                }
            }
        })

    private fun initHeaderOnChangeListener() =

        viewBinding.editTextHeader.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                isNoteEdited = true
                val text = s.toString()
                lifecycleScope.launch {
                    viewModel.intent.send(NoteIntent.SendHeader(text))
                }
            }
        })


    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is NoteState.LimitColor -> setLimitColor(it)
                    is NoteState.TimeAndDate -> setTimeAndDate(it)
                    is NoteState.Data -> setData(it.data)
                }
            }
        }
    }

    private fun setData(it: NoteData) =
        with(viewBinding) {
            editTextHeader.setText(it.header)
            editTextBody.setText(it.body)
        }

    @SuppressLint("SetTextI18n")
    private fun setTimeAndDate(it: NoteState.TimeAndDate) =
        with(viewBinding) {
            txtDate.text = "${it.date.d} ${it.date.nameOfMonth} ${it.date.y}"
            txtTime.text = "${it.time.h}:${it.time.m}"
        }


    private fun setLimitColor(it: NoteState.LimitColor) {
        val color = resources.getColor(it.limitColor, null)
        viewBinding.txtSizeLimit.setTextColor(color)
    }

    override fun destroy() {
        lifecycleScope.launch {
            viewModel.intent.send(NoteIntent.StopTimeAndDateObserving)
        }
    }

}
