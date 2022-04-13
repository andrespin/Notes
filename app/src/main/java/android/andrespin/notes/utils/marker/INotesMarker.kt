package android.andrespin.notes.utils.marker

import android.andrespin.notes.model.NoteData

interface INotesMarker {

    fun setAllNotesUnChecked(list: MutableList<NoteData>): MutableList<NoteData>

    fun setNoteChecked(note: NoteData): NoteData

    fun setNoteUnchecked(note: NoteData): NoteData

    fun isCheckedNoteFound(list: MutableList<NoteData>): Boolean

    fun getCheckedNotes(list: MutableList<NoteData>): MutableList<NoteData>

    fun setBackground(list: List<NoteData>): List<NoteData>

    fun isNoteChecked(note: NoteData): Boolean

}
