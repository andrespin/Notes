package android.andrespin.notes.notes.intent

import android.andrespin.notes.profile.my_profile.intent.ProfileIntent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

sealed class NotesIntent {

    object DownloadNotes : NotesIntent()

    object DeleteCheckedNotes : NotesIntent()

    object CancelCheckedNotes : NotesIntent()

    object SetSortByDateInAscendingOrder : NotesIntent()

    object SetSortByDateInDescendingOrder : NotesIntent()

    object SetSortByNoteSizeInAscendingOrder : NotesIntent()

    object SetSortByNoteSizeInDescendingOrder : NotesIntent()

    object CheckAuthorization : NotesIntent()

    object SynchronizeNotes : NotesIntent()

    data class LongItemClickEvent(val id: Int, val position: Int) : NotesIntent()

    data class ShortItemClickEvent(val id: Int, val position: Int) : NotesIntent()

}
