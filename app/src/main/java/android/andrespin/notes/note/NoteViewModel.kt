package android.andrespin.notes.note

import android.andrespin.notes.BaseViewModel
import android.andrespin.notes.R
import android.andrespin.notes.model.Date
import android.andrespin.notes.model.Time
import android.andrespin.notes.model.database.NoteEntity
import android.andrespin.notes.model.interactor.Interactor
import android.andrespin.notes.note.intent.NoteIntent
import android.andrespin.notes.note.intent.NoteState
import android.andrespin.notes.utils.converter.DataTypes
import android.andrespin.notes.utils.converter.TimeAndDate
import android.andrespin.notes.utils.marker.INotesMarker
import android.andrespin.notes.utils.watch.ICurrentTime
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel
@Inject constructor(
    private val provideCurrentTime: ICurrentTime,
    private val dateFormatConverter: TimeAndDate,
    private val provideMainInteractor: Interactor,
    private val provideDataTypeConverter: DataTypes,

    ) : BaseViewModel() {

    val intent = Channel<NoteIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<NoteState>(NoteState.Idle)
    val state: StateFlow<NoteState> get() = _state

    private lateinit var time: Time

    private lateinit var date: Date

    private var header = ""

    private var body = ""

    private fun setStateValue(value: NoteState) {
        _state.value = value
    }

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is NoteIntent.SendHeader -> handleHeader(it)
                    is NoteIntent.SendBody -> handleBody(it)
                    is NoteIntent.StartTimeAndDateObserving -> startTimeAndDateObserving()
                    is NoteIntent.StopTimeAndDateObserving -> stopTimeAndDateObserving()
                    is NoteIntent.SaveNote -> saveNote(it)
                    is NoteIntent.GetNoteById -> getNoteById(it)
                }
            }
        }
    }

    private suspend fun getNoteById(it: NoteIntent.GetNoteById) {
        setStateValue(
            NoteState.Idle
        )
        delay(1)
        viewModelScope.launch(Dispatchers.IO) {
            if (it.id != null) {
                val noteEntity = provideMainInteractor.getNoteById(it.id)
                val noteData = provideDataTypeConverter.convertToNoteData(noteEntity)
                setStateValue(
                    NoteState.Data(noteData)
                )
            }
        }
    }

    private fun startTimeAndDateObserving() {
        provideCurrentTime.startTimeObserving()
        viewModelScope.launch(Dispatchers.Main) {
            provideCurrentTime.currentTimeAndDate.collect {
                val convertedToTime = dateFormatConverter.convertToTime(it)
                val convertedToDate = dateFormatConverter.convertToDate(it)
                val addedNameOfMonth = dateFormatConverter.addNameOfMonth(convertedToDate)
                time = convertedToTime
                date = addedNameOfMonth
                setStateValue(
                    NoteState.TimeAndDate(
                        convertedToTime,
                        addedNameOfMonth
                    )
                )
            }
        }
    }

    private fun stopTimeAndDateObserving() {
        provideCurrentTime.stopTimeObserving()
    }

    private fun handleBody(it: NoteIntent.SendBody) {
        body = it.body
    }

    private fun handleHeader(it: NoteIntent.SendHeader) {
        header = it.header
        if (it.header.length < 30) {
            setLimitColor(R.color.red_checked)
        } else {
            setLimitColor(R.color.black)
        }
    }

    private fun saveNote() =
        viewModelScope.launch(Dispatchers.IO) {
            provideMainInteractor.saveNote(
                NoteEntity(
                    0,
                    header,
                    body,
                    time.h,
                    time.m,
                    time.s,
                    date.d,
                    date.m,
                    date.y
                )
            )
        }

    private fun updateNote(id: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            provideMainInteractor.updateNote(
                NoteEntity(
                    id,
                    header,
                    body,
                    time.h,
                    time.m,
                    time.s,
                    date.d,
                    date.m,
                    date.y
                )
            )
        }

    private fun saveNote(noteIntent: NoteIntent.SaveNote) {
        println("noteIntent.isNoteEdited ${noteIntent.isNoteEdited}")
        if (noteIntent.isNoteEdited) {
            if (header != "" || body != "") {
                if (noteIntent.id == 0 || noteIntent.id == null) {
                    saveNote()
                } else {
                    updateNote(noteIntent.id)
                }
            }
        }
    }

    private fun setLimitColor(color: Int) {
        setStateValue(
            NoteState.LimitColor(
                color
            )
        )
    }

}