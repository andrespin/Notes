package android.andrespin.notes.notes.intent

import android.andrespin.notes.State
import android.andrespin.notes.model.NoteData

sealed class NotesState : State {

    object Idle : NotesState()

    data class Notes(val list: List<NoteData>) : NotesState()

    data class OpenNote(val id: Int) : NotesState()

}
