package android.andrespin.notes.notes.intent

import android.andrespin.notes.model.NoteData

sealed class NotesEvent {

    object ShowButtons : NotesEvent()

    object HideButtons : NotesEvent()

    object Error : NotesEvent()

    data class NotesFromServer(val list: List<NoteData>, val isSynchronized: Boolean) : NotesEvent()

    data class OpenNote(val id: Int) : NotesEvent()

    data class SetNoteListWithNewVars(val list: List<NoteData>) : NotesEvent()


}