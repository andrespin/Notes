package android.andrespin.notes.model.di

import android.andrespin.notes.App
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class AppModule() {

    @Provides
    fun app(): App = App()

}