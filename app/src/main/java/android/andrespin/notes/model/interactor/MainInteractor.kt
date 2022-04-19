package android.andrespin.notes.model.interactor

import android.andrespin.notes.model.RegData
import android.andrespin.notes.model.database.NoteEntity
import android.andrespin.notes.model.repository.IRepoLocal
import android.andrespin.notes.model.repository.IRepoRemote
import com.parse.ParseObject
import com.parse.ParseQuery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainInteractor
@Inject constructor(
    private val provideRepoLocal: IRepoLocal,
    private val provideRepoRemote: IRepoRemote
) : Interactor {

    override fun setRegData(reg: RegData) = provideRepoRemote.setRegData(reg)

    override fun getCurrentLogin(login: String): ParseQuery<ParseObject>? =
        provideRepoRemote.getDataByLogin(login)

    override suspend fun getAllNotes(
        isAuthorized: Boolean,
        isConnected: Boolean
    ): List<NoteEntity> = if (isAuthorized) {
        downloadNotesRemote(isConnected)
    } else {
        downloadNotesLocal()
    }


    private suspend fun downloadNotesLocal() =
        provideRepoLocal.getAllNotes()


    private fun downloadNotesRemote(connected: Boolean): List<NoteEntity> {
        TODO("Not yet implemented")
    }


    override suspend fun saveNote(
        noteEntity: NoteEntity,
        isAuthorized: Boolean,
        isConnected: Boolean
    ) {
        if (isAuthorized) {
            saveNoteOnServer(noteEntity)
            saveNoteToDb(noteEntity)
        } else {
            saveNoteToDb(noteEntity)
        }
    }

    override suspend fun updateNote(
        noteEntity: NoteEntity,
        isAuthorized: Boolean,
        isConnected: Boolean
    ) {
        if (isAuthorized) {
            // TODO
        } else {
            provideRepoLocal.updateNote(noteEntity)
        }

    }

    override suspend fun deleteNote(
        noteEntity: NoteEntity,
        isAuthorized: Boolean,
        isConnected: Boolean
    ) {
        if (isAuthorized) {
            // TODO
        } else {
            provideRepoLocal.deleteNote(noteEntity)
        }
    }

    override suspend fun deleteNotes(
        noteEntity: MutableList<NoteEntity>,
        isAuthorized: Boolean,
        isConnected: Boolean
    ) {
        if (isAuthorized) {
            // TODO
        } else {
            provideRepoLocal.deleteNoteList(noteEntity)
        }
    }

    private suspend fun saveNoteToDb(noteEntity: NoteEntity) {
        provideRepoLocal.insertNote(noteEntity)
    }

    private fun saveNoteOnServer(noteEntity: NoteEntity) {

    }

    override suspend fun saveNoteList(
        noteList: List<NoteEntity>,
        isAuthorized: Boolean,
        isConnected: Boolean
    ) {
        if (isAuthorized) {
            saveNoteListToDb(noteList)
            saveNoteListOnServer(noteList, isConnected)
        } else {
            saveNoteListToDb(noteList)
        }
    }

    override suspend fun getNoteById(id: Int, isAuthorized: Boolean, isConnected: Boolean) =
        provideRepoLocal.getNoteById(id)

    private suspend fun saveNoteListToDb(noteListEntity: List<NoteEntity>) {
        provideRepoLocal.insertNoteList(noteListEntity)
    }

    private fun saveNoteListOnServer(noteListEntity: List<NoteEntity>, isConnected: Boolean) {
        TODO("Not yet implemented")
    }


}