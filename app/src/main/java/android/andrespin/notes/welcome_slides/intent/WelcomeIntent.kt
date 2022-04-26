package android.andrespin.notes.welcome_slides.intent

sealed class WelcomeIntent {

    data class NextSlide(val currentItem: Int, val size: Int) : WelcomeIntent()

}
