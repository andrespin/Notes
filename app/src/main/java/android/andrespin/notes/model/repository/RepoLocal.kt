package android.andrespin.notes.model.repository

import android.andrespin.notes.model.database.NoteDao
import android.andrespin.notes.model.database.NoteEntity
import android.database.sqlite.SQLiteConstraintException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoLocal
@Inject constructor(
    private val provideNoteDao: NoteDao
) : IRepoLocal {

    override suspend fun getAllNotes(): List<NoteEntity> = provideNoteDao.getAllNotes()

    override suspend fun getNoteById(id: Int): NoteEntity = provideNoteDao.getNote(id)

    override suspend fun updateNote(noteEntity: NoteEntity) {
        provideNoteDao.updateNote(noteEntity)
    }

    override suspend fun deleteNote(noteEntity: NoteEntity) {
        provideNoteDao.deleteNote(noteEntity)
    }

    override suspend fun insertNote(noteEntity: NoteEntity) {
        provideNoteDao.insertNote(noteEntity)
    }

    override suspend fun insertNoteList(noteListEntity: List<NoteEntity>) {
        for (element in noteListEntity) {
            try {
                provideNoteDao.insertNote(element)
            } catch (e: SQLiteConstraintException) {
                provideNoteDao.updateNote(element)
                e.printStackTrace()
            }
        }
    }

    override suspend fun deleteNoteList(noteListEntity: List<NoteEntity>) {
        for (element in noteListEntity) {
            provideNoteDao.deleteNote(element)
        }
    }

}