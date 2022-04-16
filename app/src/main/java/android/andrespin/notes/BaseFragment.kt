package android.andrespin.notes

import android.andrespin.notes.model.RegData
import android.andrespin.notes.model.login_key
import android.andrespin.notes.model.password_key
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    protected lateinit var viewModel: VM

    abstract val viewModelClass: Class<VM>

    private lateinit var _viewBinding: VB

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    protected val viewBinding: VB
        get() = _viewBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("Fragment lifecycle", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        Log.d("Fragment lifecycle", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Fragment lifecycle", "onCreateView")
        _viewBinding = bindingInflater.invoke(inflater, container, false)
        return _viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        observeData()
        Log.d("Fragment lifecycle", "onViewCreated")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("Fragment lifecycle", "onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Fragment lifecycle", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Fragment lifecycle", "onResume")
    }


    override fun onPause() {
        super.onPause()
        pause()
        Log.d("Fragment lifecycle", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Fragment lifecycle", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("Fragment lifecycle", "onDestroyView")
    }

    open fun pause() {

    }

    override fun onDestroy() {
        super.onDestroy()
        destroy()
        Log.d("Fragment lifecycle", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("Fragment lifecycle", "onDetach")
    }

    open fun destroy() {}

    abstract fun setUpViews()

    open fun observeView() {}

    open fun observeData() {}

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(viewModelClass)
    }

    fun toastMessage(text: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(requireContext(), text, duration)
        toast.show()
    }

}