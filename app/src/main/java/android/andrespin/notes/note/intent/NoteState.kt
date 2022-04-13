package android.andrespin.notes.note.intent

import android.andrespin.notes.model.Date
import android.andrespin.notes.model.NoteData
import android.andrespin.notes.model.Time

sealed class NoteState {
    object Idle : NoteState()

    data class Data(val data: NoteData) : NoteState()

    data class HeaderData(val header: String, val limitColor: Int) : NoteState()

    data class LimitColor(val limitColor: Int) : NoteState()

    data class TimeAndDate(val time: Time, val date: Date): NoteState()

}







