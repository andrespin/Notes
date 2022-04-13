package android.andrespin.notes.utils

import android.andrespin.notes.databinding.FragmentNotesBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

class Factory : ViewModelFactory {

    override fun getViewBinding(
        vb: ViewBinding,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewBinding =
        when (vb) {
            is FragmentNotesBinding -> {
                FragmentNotesBinding.inflate(inflater, container, false)
            }
            else -> {
                throw Exception("Binding nor found")
            }
        }

}