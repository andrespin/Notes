package android.andrespin.notes.utils.sorter

import android.andrespin.notes.model.NoteData
import android.andrespin.notes.model.database.NoteEntity

interface ISorter {

    suspend fun setSortByDateInAscendingOrder(notes: MutableList<NoteData>): MutableList<NoteData>

    suspend fun setSortByNoteSizeInAscendingOrder(notes: MutableList<NoteData>): MutableList<NoteData>

    suspend fun setSortByNoteSizeInDescendingOrder(notes: MutableList<NoteData>): MutableList<NoteData>

    suspend fun setSortByDateInDescendingOrder(notes: MutableList<NoteData>): MutableList<NoteData>

    /**
    Принимает заметки, полученные от сервера, и заметки, загруженные из внутренней
    базы данных Room, сопоставляет их и возвращает объект SorterNotes, содержащий
    заметки (missingNotesDb), которые нужно загрузить в базу данных, а также
    заметки (missingNotesServer), которые нужно загрузить на сервер
     */
    suspend fun sortNotesForSyncing(notesDb: List<NoteEntity>, notesServer: List<NoteEntity>):
            SorterNotes

}

data class SorterNotes(
    val missingNotesDb: List<NoteEntity>,
    val missingNotesServer: List<NoteEntity>
)