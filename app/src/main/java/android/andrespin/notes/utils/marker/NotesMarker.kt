package android.andrespin.notes.utils.marker

import android.andrespin.notes.model.NoteData
import android.andrespin.notes.model.colorCheckedList
import android.andrespin.notes.model.colorList
import android.view.View

class NotesMarker : INotesMarker {

    override fun setAllNotesUnChecked(list: MutableList<NoteData>): MutableList<NoteData> {
        for (i in 0 until list.size) {
            if (list[i].isChecked) {
                list[i] = setNoteUnchecked(list[i])
            }
        }
        return list
    }

    override fun setNoteChecked(note: NoteData): NoteData {
        note.isChecked = true
        note.checkedVisibility = View.VISIBLE
        return note
    }

    override fun setNoteUnchecked(note: NoteData): NoteData {
        note.isChecked = false
        note.checkedVisibility = View.INVISIBLE
        return note
    }

    override fun isCheckedNoteFound(list: MutableList<NoteData>): Boolean {
        for (i in 0 until list.size) {
            if (list[i].isChecked) {
                return true
            }
        }
        return false
    }

    override fun getCheckedNotes(list: MutableList<NoteData>): MutableList<NoteData> {
        val c = mutableListOf<NoteData>()
        for (i in 0 until list.size) {
            if (list[i].isChecked) {
                c.add(list[i])
            }
        }
        return c
    }

    override fun setBackground(list: List<NoteData>): List<NoteData> {
        var j = 0
        for (i in 0 until list.size) {
            if (j > 3) j = 0
            list[i].background = colorList[j]
            list[i].checkedBackground = colorCheckedList[j]
            j++
        }
        return list
    }

    override fun isNoteChecked(note: NoteData): Boolean =
        note.isChecked

}