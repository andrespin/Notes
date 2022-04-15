package android.andrespin.notes.utils.sorter

import android.andrespin.notes.model.NoteData

class Sorter : ISorter, SorterTools() {
    override suspend fun setSortByDateInAscendingOrder(notes: MutableList<NoteData>)
            : MutableList<NoteData> = sortNotesAccordingTimeAndDateInDescendingOrder(notes)

    override suspend fun setSortByNoteSizeInAscendingOrder(notes: MutableList<NoteData>)
            : MutableList<NoteData> = notes.sortedBy {
        it.body + it.header
    }.toMutableList()


    override suspend fun setSortByNoteSizeInDescendingOrder(notes: MutableList<NoteData>)
            : MutableList<NoteData> = notes.sortedByDescending {
        it.body + it.header
    }.toMutableList()

    override suspend fun setSortByDateInDescendingOrder(notes: MutableList<NoteData>)
            : MutableList<NoteData> = sortNotesAccordingTimeAndDateInAscendingOrder(notes)
}