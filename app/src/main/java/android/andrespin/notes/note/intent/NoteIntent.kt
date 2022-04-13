package android.andrespin.notes.note.intent

import android.andrespin.notes.model.NoteData

sealed class NoteIntent {

    object StartTimeAndDateObserving : NoteIntent()

    object StopTimeAndDateObserving : NoteIntent()

    data class SaveNote(val id: Int?, val isNoteEdited: Boolean) : NoteIntent()

    data class GetNoteById(val id: Int?) : NoteIntent()

    data class SendHeader(val header: String) : NoteIntent()
    data class SendBody(val body: String) : NoteIntent()
  //  data class SaveNoteInDb(val data: NoteData) : NoteIntent()

}
