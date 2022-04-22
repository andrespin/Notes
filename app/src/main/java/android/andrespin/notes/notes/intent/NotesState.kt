package android.andrespin.notes.notes.intent

import android.andrespin.notes.State
import android.andrespin.notes.model.NoteData
import android.andrespin.notes.model.RegData

sealed class NotesState : State {

    object Idle : NotesState()

    object NotAuthorized : NotesState()

    data class Notes(val list: List<NoteData>) : NotesState()

    data class Authorized(val regData: RegData) : NotesState()

}
