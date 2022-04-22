package android.andrespin.notes.model.interactor

import android.andrespin.notes.model.RegData
import android.andrespin.notes.model.database.NoteEntity
import android.andrespin.notes.model.repository.IRepoLocal
import android.andrespin.notes.model.repository.IRepoRemote
import android.andrespin.notes.profile.entrance.intent.EntranceEvent
import android.andrespin.notes.utils.converter.DataTypes
import android.andrespin.notes.utils.sorter.ISorter
import android.andrespin.notes.utils.sorter.SorterNotes
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception
import java.util.concurrent.CompletableFuture
import javax.inject.Inject
import javax.inject.Singleton

data class FlowTest(val n: Int)

data class ServerState(val objects: List<ParseObject>?, val e: Exception?)

//data class SignTest(val s: String)

@Singleton
class MainInteractor
@Inject constructor(
    private val provideRepoLocal: IRepoLocal,
    private val provideRepoRemote: IRepoRemote,
    private val provideSorter: ISorter,
    private val provideConverter: DataTypes
) : Interactor {

    override fun setRegData(reg: RegData) = provideRepoRemote.setRegData(reg)

    override fun getCurrentLogin(login: String): ParseQuery<ParseObject>? =
        provideRepoRemote.getRegDataByLogin(login)

    override suspend fun syncNotes(
        isSyncingOn: Boolean,
        login: String,
        scope: CoroutineScope?
    ): Flow<SyncState> = flow {

    }


    override suspend fun synchronizeNotes(
        isSyncingOn: Boolean,
        login: String,
        scope: CoroutineScope?
    ): Flow<SyncState> = flow {

    }

    override suspend fun getAllNotes(): List<NoteEntity> {
        return provideRepoLocal.getAllNotes()
    }

    override suspend fun getAllNotes(
        login: String
    ): ParseQuery<ParseObject>? =

        provideRepoRemote.getAllNotes(login)


    override suspend fun saveNotes(notes: List<NoteEntity>) {
        provideRepoLocal.insertNoteList(notes)
    }

    override suspend fun saveNotes(notes: List<NoteEntity>, login: String) {
        provideRepoRemote.setNotes(notes, login)
    }

    override suspend fun saveMissingNotes(
        missingNotesDb: List<NoteEntity>,
        missingNotesServer: List<NoteEntity>,
        login: String
    ) {
        provideRepoRemote.setNotes(missingNotesServer, login)
        provideRepoLocal.insertNoteList(missingNotesDb)
    }

    fun flowTest(): Flow<FlowTest> = flow {
        var j = 0
        for (i in 0 until 10) {
            j++
            emit(FlowTest(j))
        }
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