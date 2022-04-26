package android.andrespin.notes.main

import android.andrespin.notes.R
import android.andrespin.notes.databinding.ActivityMainBinding
import android.andrespin.notes.main.intent.MainEvent
import android.andrespin.notes.main.intent.MainIntent
import android.andrespin.notes.main.intent.MainState
import android.andrespin.notes.notes.intent.NotesState
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_welcome.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : BaseActivity() {


    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater())
        setContentView(viewBinding.root)
        //  initViewBinding()
        initViewModel()
        initNavController()
        checkIfFirst()
        observeViewModel()
    }

//    binding = ResultProfileBinding.inflate(getLayoutInflater());
//    View view = binding.getRoot();
//    setContentView(view);

    private fun initViewBinding() {
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater())
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun initNavController() {
        val navController = this.findNavController(R.id.nav_host_fragment)
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        navView.setupWithNavController(navController)
    }

    private fun checkIfFirst() {
        lifecycleScope.launch {
            viewModel.intent.send(MainIntent.CheckIfFirst)
        }
    }


    private fun observeViewModel() {
        observeState()
        observeEvent()
    }


    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is MainState.FirstLaunch -> showPresentation()
                    is MainState.Launch -> skipPresentation()
                }
            }
        }
    }

    private fun observeEvent() {
        lifecycleScope.launch {
            viewModel.event.collect {
                when (it) {
                    is MainEvent.ActiveSlide -> activateSlide(it)
                }

            }
        }
    }

    private fun activateSlide(it: MainEvent) {

    }



    private fun showPresentation() = with(viewBinding) {
        bottomNavView.visibility = View.GONE
        findNavController(R.id.nav_host_fragment).navigateUp()
        findNavController(R.id.nav_host_fragment).navigate(R.id.welcome)
    }

    private fun skipPresentation() {
        viewBinding.bottomNavView.visibility = View.VISIBLE
    }

//    fun setIndicators(slideNumber: Int) {
//        when (slideNumber) {
////            0 -> vb?.firstIndicator?.setImageResource(R.drawable.swipe_indicator_active)
////            1 -> vb?.secondIndicator?.setImageResource(R.drawable.swipe_indicator_active)
////            2 -> vb?.thirdIndicator?.setImageResource(R.drawable.swipe_indicator_active)
////            3 -> vb?.fourthIndicator?.setImageResource(R.drawable.swipe_indicator_active)
//        }
//    }


}