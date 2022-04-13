package android.andrespin.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = bindingInflater.invoke(inflater, container, false)
        return _viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        observeData()
    }

    override fun onPause() {
        super.onPause()
        pause()
    }

    open fun pause() {

    }


    override fun onDestroy() {
        super.onDestroy()
        destroy()
    }

    open fun destroy() {}

    abstract fun setUpViews()

    open fun observeView() {}

    open fun observeData() {}

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(viewModelClass)
    }

//    fun NavController.safeNavigate(direction: Int) {
//        val controller = findNavController()
//        if (controller.currentDestination?.id == R.id.notes) {
//            controller.navigate(direction)
//        }
//    }
//
//    fun NavController.safeNavigate(direction: Int, bundle: Bundle) {
//        val controller = findNavController()
//        if (controller.currentDestination?.id == R.id.notes) {
//            controller.navigate(direction, bundle)
//        }
//    }

}