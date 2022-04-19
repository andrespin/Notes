package android.andrespin.notes.profile.entrance.intent

sealed class EntranceState {

    object Idle : EntranceState()

    object LogIn : EntranceState()

    object FieldsAreNotFilled : EntranceState()

    object PassIsNotCorrect : EntranceState()

    object LoginIsNotFound : EntranceState()

    data class Error(val msg: String) : EntranceState()


}