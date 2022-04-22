package android.andrespin.notes.model.interactor

import android.andrespin.notes.model.database.NoteEntity

sealed class SyncState {

    data class Success(val notesFromServer: List<NoteEntity>) : SyncState()

    data class Error(val error: String) : SyncState()

}
