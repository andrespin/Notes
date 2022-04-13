package android.andrespin.notes.profile.my_profile.intent

import android.andrespin.notes.note.intent.NoteState

sealed class ProfileState {

    object Idle : ProfileState()

    object ProfileIsAuthorized : ProfileState()

    object ProfileIsNotAuthorized : ProfileState()

}