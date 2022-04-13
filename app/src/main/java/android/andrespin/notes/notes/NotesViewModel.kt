package android.andrespin.notes.notes

import android.andrespin.notes.BaseViewModel
import android.andrespin.notes.model.NoteData
import android.andrespin.notes.model.colorCheckedList
import android.andrespin.notes.model.colorList
import android.andrespin.notes.model.database.NoteEntity
import android.andrespin.notes.model.interactor.Interactor
import android.andrespin.notes.notes.intent.NotesEvent
import android.andrespin.notes.notes.intent.NotesIntent
import android.andrespin.notes.notes.intent.NotesState
import android.andrespin.notes.utils.converter.DataTypes
import android.andrespin.notes.utils.converter.TimeAndDate
import android.andrespin.notes.utils.marker.INotesMarker
import android.view.View
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ObsoleteCoroutinesApi
@HiltViewModel
class NotesViewModel
@Inject constructor(
    private val provideMainInteractor: Interactor,
    private val provideConverter: DataTypes,
    private val marker: INotesMarker
) : BaseViewModel() {

    val intent = Channel<NotesIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<NotesState>(NotesState.Idle)
    val state: StateFlow<NotesState> get() = _state

    private val _eventChannel = Channel<NotesEvent>(Channel.CONFLATED)

    val event: Channel<NotesEvent> = _eventChannel

    private var noteDataList = mutableListOf<NoteData>()

    private var areNotesChecked = false

    private fun setStateValue(value: NotesState) {
        _state.value = value
    }

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is NotesIntent.DownloadNotes -> downloadNotes()
                    is NotesIntent.ShortItemClickEvent -> shortItemClick(it)
                    is NotesIntent.LongItemClickEvent -> longItemClick(it)
                    is NotesIntent.DeleteCheckedNotes -> deleteCheckedNotes()
                }
            }
        }
    }

    private suspend fun deleteCheckedNotes() {
            var notes = mutableListOf<NoteEntity>()
            coroutineScope {
                val get = launch(Dispatchers.Main, start = CoroutineStart.LAZY) {
                    val n = marker.getCheckedNotes(noteDataList)
                    notes = provideConverter.convertToNoteEntityList(n) as MutableList
                }

                val delete = launch(Dispatchers.IO, start = CoroutineStart.LAZY) {
                    provideMainInteractor.deleteNotes(notes)
                }

                val updateAdapter = launch(Dispatchers.IO, start = CoroutineStart.LAZY) {
                    provideMainInteractor.deleteNotes(notes)

                    val n = provideConverter.convertToNoteDataList(provideMainInteractor.getAllNotes())
                                as MutableList<NoteData>
                    noteDataList = marker.setBackground(n) as MutableList<NoteData>
                    setStateValue(
                        NotesState.Notes(noteDataList)
                    )
                }
                get.start()
                get.join()
                delete.start()
                delete.join()
                updateAdapter.start()
                updateAdapter.join()
            }
    }

    private fun longItemClick(it: NotesIntent.LongItemClickEvent) {

        if (marker.isCheckedNoteFound(noteDataList)) {

            if (marker.isNoteChecked(noteDataList[it.position])) {
                noteDataList[it.position] = marker.setNoteUnchecked(noteDataList[it.position])
            } else {
                noteDataList[it.position] = marker.setNoteChecked(noteDataList[it.position])
            }

            viewModelScope.launch(Dispatchers.Main) {
                _eventChannel.send(NotesEvent.SetNoteListWithNewVars(noteDataList))
            }

        } else {
            noteDataList[it.position] = marker.setNoteChecked(noteDataList[it.position])
            viewModelScope.launch(Dispatchers.Main) {
                _eventChannel.send(NotesEvent.SetNoteListWithNewVars(noteDataList))
            }
        }

        if (marker.isCheckedNoteFound(noteDataList)) {
            showBtn()
        } else {
            hideBtn()
        }

    }

    private fun showBtn() = viewModelScope.launch {
        _eventChannel.send(NotesEvent.ShowButtons)
    }

    private fun hideBtn() = viewModelScope.launch {
        _eventChannel.send(NotesEvent.HideButtons)
    }

    private fun shortItemClick(it: NotesIntent.ShortItemClickEvent) {

        if (marker.isCheckedNoteFound(noteDataList)) {
            if (marker.isNoteChecked(noteDataList[it.position])) {
                noteDataList[it.position] = marker.setNoteUnchecked(noteDataList[it.position])
                viewModelScope.launch {
                    _eventChannel.send(NotesEvent.SetNoteListWithNewVars(noteDataList))
                }
            } else {
                noteDataList[it.position] = marker.setNoteChecked(noteDataList[it.position])
                viewModelScope.launch {
                    _eventChannel.send(NotesEvent.SetNoteListWithNewVars(noteDataList))
                }
            }
        } else {
            viewModelScope.launch {
                _eventChannel.send(NotesEvent.OpenNote(it.id))
            }
        }

        if (marker.isCheckedNoteFound(noteDataList)) {
            showBtn()
        } else {
            hideBtn()
        }

    }

    private fun downloadNotes() {

        println("downloadNotes")
        viewModelScope.launch {
            val notes = provideMainInteractor.getAllNotes()
            val convertToNoteData = provideConverter.convertToNoteDataList(notes)
            noteDataList = marker.setBackground(convertToNoteData) as MutableList<NoteData>
            println("downloadNotes last ${noteDataList[noteDataList.lastIndex]}")
            setStateValue(
                NotesState.Notes(noteDataList)
            )
        }
    }
}