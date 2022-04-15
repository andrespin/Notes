package android.andrespin.notes.profile.my_profile.intent

import android.andrespin.notes.model.RegData

sealed class ProfileIntent {

    object TurnSyncingOn : ProfileIntent()

    object TurnSyncingOff : ProfileIntent()

    object LogOut : ProfileIntent()

    data class LogIn(val login: String, val pass: String) : ProfileIntent()

    data class LoggedIn(val reg: RegData) : ProfileIntent()

}