package android.andrespin.notes.model.repository

import android.andrespin.notes.model.database.NoteEntity

interface IRepoLocal {

    suspend fun getAllNotes(): List<NoteEntity>

    suspend fun getNoteById(id: Int): NoteEntity

    suspend fun updateNote(noteEntity: NoteEntity)

    suspend fun deleteNote(noteEntity: NoteEntity)

    suspend fun insertNote(noteEntity: NoteEntity)

    suspend fun insertNoteList(noteListEntity: List<NoteEntity>)

    suspend fun deleteNoteList(noteListEntity: List<NoteEntity>)

}