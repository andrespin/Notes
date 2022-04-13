package android.andrespin.notes.profile.my_profile.intent

sealed class ProfileIntent {

    object TurnSyncingOn : ProfileIntent()

    object TurnSyncingOff : ProfileIntent()

}