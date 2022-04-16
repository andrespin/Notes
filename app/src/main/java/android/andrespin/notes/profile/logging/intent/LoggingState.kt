package android.andrespin.notes.profile.logging.intent

sealed class LoggingState {

    object Idle : LoggingState()

    object LoginIsBusy : LoggingState()

    object LoginIsOk : LoggingState()

    object PassIsNotRepeated : LoggingState()

    object PassIsOk : LoggingState()

    object RegDataCorrect : LoggingState()

}