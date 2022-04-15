package android.andrespin.notes.model.di

import android.andrespin.notes.utils.converter.Converter
import android.andrespin.notes.utils.converter.DataTypes
import android.andrespin.notes.utils.converter.TimeAndDate
import android.andrespin.notes.utils.marker.INotesMarker
import android.andrespin.notes.utils.marker.NotesMarker
import android.andrespin.notes.utils.sorter.ISorter
import android.andrespin.notes.utils.sorter.Sorter
import android.andrespin.notes.utils.watch.CurrentTime
import android.andrespin.notes.utils.watch.ICurrentTime
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@InstallIn(ViewModelComponent::class)
@Module
class ApplicationModule {

    @Provides
    internal fun provideCurrentTime(): ICurrentTime = CurrentTime(
        CoroutineScope(
            Dispatchers.Main
        )
    )

    @Provides
    internal fun provideConverter(): TimeAndDate = Converter()

    @Provides
    internal fun provideConv(): DataTypes = Converter()

    @Provides
    internal fun provideMarker(): INotesMarker = NotesMarker()

    @Provides
    fun provideSorter(): ISorter = Sorter()

}