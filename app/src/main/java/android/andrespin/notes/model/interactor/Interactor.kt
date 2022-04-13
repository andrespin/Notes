package android.andrespin.notes.model.interactor

import android.andrespin.notes.model.database.NoteEntity

interface Interactor {

    suspend fun getAllNotes(
        isAuthorized: Boolean = false,
        isConnected: Boolean = false
    ): List<NoteEntity>

    suspend fun saveNote(
        noteEntity: NoteEntity,
        isAuthorized: Boolean = false,
        isConnected: Boolean = false
    )

    suspend fun updateNote(
        noteEntity: NoteEntity,
        isAuthorized: Boolean = false,
        isConnected: Boolean = false
    )

    suspend fun deleteNote(
        noteEntity: NoteEntity,
        isAuthorized: Boolean = false,
        isConnected: Boolean = false
    )

    suspend fun deleteNotes(
        noteEntity: MutableList<NoteEntity>,
        isAuthorized: Boolean = false,
        isConnected: Boolean = false
    )

    suspend fun saveNoteList(
        noteList: List<NoteEntity>,
        isAuthorized: Boolean = false,
        isConnected: Boolean = false
    )

    suspend fun getNoteById(
        id: Int,
        isAuthorized: Boolean = false,
        isConnected: Boolean = false
    ): NoteEntity

}