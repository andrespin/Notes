package android.andrespin.notes.utils.sorter

import android.andrespin.notes.model.NoteData
import android.andrespin.notes.model.database.NoteEntity

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

    override suspend fun sortNotesForSyncing(
        notesDb: List<NoteEntity>,
        notesServer: List<NoteEntity>
    ): SorterNotes {
        val list = mutableListOf<NoteEntity>()
        val missingNotesDb = mutableListOf<NoteEntity>()
        val missingNotesServer = mutableListOf<NoteEntity>()
        list.addAll(notesDb)
        for (i in 0 until notesServer.size) {
            if (!list.contains(notesServer[i])) {
                list.add(notesServer[i])
            }
        }

        for (i in 0 until list.size) {
            if (!notesDb.contains(list[i])) {
                missingNotesDb.add(list[i])
            }

            if (!notesServer.contains(list[i])) {
                missingNotesServer.add(list[i])
            }
        }
        return SorterNotes(
            missingNotesDb,
            missingNotesServer
        )
    }


}