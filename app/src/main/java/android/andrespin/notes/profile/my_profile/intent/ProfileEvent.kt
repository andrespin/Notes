package android.andrespin.notes.profile.my_profile.intent

import android.andrespin.notes.model.di.SyncingResult

sealed class ProfileEvent {

    object SyncOff : ProfileEvent()

    object SyncOnSuccess : ProfileEvent()

    object SyncOnError : ProfileEvent()

}