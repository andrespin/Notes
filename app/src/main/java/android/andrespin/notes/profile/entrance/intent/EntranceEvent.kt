package android.andrespin.notes.profile.entrance.intent

import android.andrespin.notes.profile.logging.intent.LoggingState

sealed class EntranceEvent {

    object FieldsAreNotFilled : EntranceEvent()

    object PassIsNotCorrect : EntranceEvent()

    object LoginIsNotFound : EntranceEvent()

}