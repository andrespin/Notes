package android.andrespin.notes.profile.entrance.intent

import android.andrespin.notes.profile.my_profile.intent.ProfileState

sealed class EntranceState {

    object Idle : EntranceState()

    object LogIn : EntranceState()

    object Error : EntranceState()

}