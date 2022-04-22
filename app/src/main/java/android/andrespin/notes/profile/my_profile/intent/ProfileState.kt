package android.andrespin.notes.profile.my_profile.intent

import android.andrespin.notes.model.RegData

sealed class ProfileState {

    object Idle : ProfileState()

    object Loading : ProfileState()

    object Error : ProfileState()

    object Success : ProfileState()

    object SyncOff : ProfileState()

    data class ProfileIsAuthorized(
        val data: RegData
    ) : ProfileState()

    object ProfileIsNotAuthorized : ProfileState()


}