package android.andrespin.notes.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

interface ViewModelFactory {

    fun getViewBinding(
        vb: ViewBinding,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewBinding

}