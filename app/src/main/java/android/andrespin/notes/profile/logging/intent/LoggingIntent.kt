package android.andrespin.notes.profile.logging.intent

sealed class LoggingIntent {

    data class Register(
        val login: String,
        val pass1: String,
        val pass2: String
    ) : LoggingIntent()

    object SaveRegData : LoggingIntent()

}