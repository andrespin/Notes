package android.andrespin.notes.profile.entrance.intent

import android.andrespin.notes.model.RegData

sealed class EntranceIntent {

    data class LogIn(val reg: RegData) : EntranceIntent()

    object Click : EntranceIntent()

}