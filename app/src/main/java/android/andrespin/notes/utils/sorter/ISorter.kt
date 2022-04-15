package android.andrespin.notes.utils.sorter

import android.andrespin.notes.model.NoteData

interface ISorter {

    suspend fun setSortByDateInAscendingOrder(notes: MutableList<NoteData>): MutableList<NoteData>

    suspend fun setSortByNoteSizeInAscendingOrder(notes: MutableList<NoteData>): MutableList<NoteData>

    suspend fun setSortByNoteSizeInDescendingOrder(notes: MutableList<NoteData>): MutableList<NoteData>

    suspend fun setSortByDateInDescendingOrder(notes: MutableList<NoteData>): MutableList<NoteData>

}