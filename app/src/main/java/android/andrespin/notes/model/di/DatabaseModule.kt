package android.andrespin.notes.model.di

import android.andrespin.notes.model.database.Database
import android.andrespin.notes.model.database.NoteDao
import android.andrespin.notes.model.interactor.Interactor
import android.andrespin.notes.model.interactor.MainInteractor
import android.andrespin.notes.model.repository.IRepoLocal
import android.andrespin.notes.model.repository.IRepoRemote
import android.andrespin.notes.model.repository.RepoLocal
import android.andrespin.notes.model.repository.RepoRemote
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ViewModelComponent::class)
@Module
class DatabaseModule {

    @Provides
    internal fun createDatabase(@ApplicationContext appContext: Context): Database {
        return Room.databaseBuilder(appContext, Database::class.java, "db.1")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    internal fun provideNoteDao(db: Database): NoteDao =
        db.noteDao()

    @Provides
    internal fun provideRepoLocal(noteDao: NoteDao): IRepoLocal =
        RepoLocal(noteDao)

    @Provides
    internal fun provideRepoRemote(): IRepoRemote =
        RepoRemote()

    @Provides
    internal fun provideMainInteractor(
        repoLocal: IRepoLocal,
        repoRemote: IRepoRemote,
    ): Interactor =
        MainInteractor(repoLocal, repoRemote)

}