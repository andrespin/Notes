package android.andrespin.notes.notes.intent

sealed class NotesIntent {


    object DownloadNotes : NotesIntent()

    object DeleteCheckedNotes : NotesIntent()

    object CancelCheckedNotes : NotesIntent()

    data class LongItemClickEvent(val id: Int, val position: Int) : NotesIntent()

    data class ShortItemClickEvent(val id: Int, val position: Int) : NotesIntent()

}
