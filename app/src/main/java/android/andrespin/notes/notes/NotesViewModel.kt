package android.andrespin.notes.notes

import android.andrespin.notes.BaseViewModel
import android.andrespin.notes.model.NoteData
import android.andrespin.notes.model.RegData
import android.andrespin.notes.model.database.NoteEntity
import android.andrespin.notes.model.di.RegPreference
import android.andrespin.notes.model.interactor.Interactor
import android.andrespin.notes.model.login_key
import android.andrespin.notes.model.password_key
import android.andrespin.notes.notes.intent.NotesEvent
import android.andrespin.notes.notes.intent.NotesIntent
import android.andrespin.notes.notes.intent.NotesState
import android.andrespin.notes.utils.converter.DataTypes
import android.andrespin.notes.utils.marker.INotesMarker
import android.andrespin.notes.utils.sorter.ISorter
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
    private val marker: INotesMarker,
    private val sorter: ISorter,
    private val regPreference: RegPreference

) : BaseViewModel() {

    val intent = Channel<NotesIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<NotesState>(NotesState.Idle)
    val state: StateFlow<NotesState> get() = _state

    private val _eventChannel = Channel<NotesEvent>(Channel.CONFLATED)

    val event: Channel<NotesEvent> = _eventChannel

    private var noteDataList = mutableListOf<NoteData>()

    private fun setStateValue(value: NotesState) {
        _state.value = value
    }

    init {
        checkAuthorization()
        handleIntent()
    }

    private fun checkAuthorization() {
        val login = regPreference.getLogin()
        val password = regPreference.getPassword()
        if (login != null && password != null) {
            setStateValue(
                NotesState.Authorized(
                    RegData(
                        login,
                        password
                    )
                )
            )
        } else {
            setStateValue(
                NotesState.NotAuthorized
            )
        }
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is NotesIntent.DownloadNotes -> downloadNotes()
                    is NotesIntent.ShortItemClickEvent -> shortItemClick(it)
                    is NotesIntent.LongItemClickEvent -> longItemClick(it)
                    is NotesIntent.DeleteCheckedNotes -> deleteCheckedNotes()
                    is NotesIntent.CancelCheckedNotes -> cancelCheckedNotes()
                    is NotesIntent.SetSortByNoteSizeInAscendingOrder ->
                        setSortByNoteSizeInAscendingOrder()
                    is NotesIntent.SetSortByNoteSizeInDescendingOrder ->
                        setSortByNoteSizeInDescendingOrder()
                    is NotesIntent.SetSortByDateInAscendingOrder ->
                        setSortByDateInAscendingOrder()
                    is NotesIntent.SetSortByDateInDescendingOrder ->
                        setSortByDateInDescendingOrder()
                }
            }
        }
    }

    private suspend fun setSortByNoteSizeInAscendingOrder() {
        setStateValue(NotesState.Idle)
        noteDataList = sorter.setSortByNoteSizeInAscendingOrder(noteDataList)
        delay(1)
        setStateValue(NotesState.Notes(noteDataList))
    }

    private suspend fun setSortByNoteSizeInDescendingOrder() {
        setStateValue(NotesState.Idle)
        noteDataList = sorter.setSortByNoteSizeInDescendingOrder(noteDataList)
        delay(1)
        setStateValue(NotesState.Notes(noteDataList))
    }

    private suspend fun setSortByDateInAscendingOrder() {
        setStateValue(NotesState.Idle)
        noteDataList = sorter.setSortByDateInAscendingOrder(noteDataList)
        delay(1)
        setStateValue(NotesState.Notes(noteDataList))
    }

    private suspend fun setSortByDateInDescendingOrder() {
        setStateValue(NotesState.Idle)
        noteDataList = sorter.setSortByDateInDescendingOrder(noteDataList)
        delay(1)
        setStateValue(NotesState.Notes(noteDataList))
    }

    private suspend fun cancelCheckedNotes() {
        hideBtn()
        coroutineScope {
            launch(Dispatchers.Main) {
                noteDataList = marker.setAllNotesUnChecked(noteDataList)
                setStateValue(
                    NotesState.Idle
                )
                delay(1)
                setStateValue(
                    NotesState.Notes(noteDataList)
                )
            }
        }
    }

    private suspend fun deleteCheckedNotes() {
        hideBtn()
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