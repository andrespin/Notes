package android.andrespin.notes.notes

import android.andrespin.notes.BaseFragment
import android.andrespin.notes.R
import android.andrespin.notes.databinding.FragmentNotesBinding
import android.andrespin.notes.model.*
import android.andrespin.notes.notes.adapter.NotesAdapter
import android.andrespin.notes.notes.intent.NotesEvent
import android.andrespin.notes.notes.intent.NotesIntent
import android.andrespin.notes.notes.intent.NotesState
import android.andrespin.notes.profile.my_profile.intent.ProfileIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesFragment() : BaseFragment<FragmentNotesBinding, NotesViewModel>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNotesBinding
        get() = FragmentNotesBinding::inflate

    override val viewModelClass: Class<NotesViewModel>
        get() = NotesViewModel::class.java

    private val adapter: NotesAdapter by lazy { NotesAdapter(requireContext(), viewModel, this) }

    override fun setUpViews() {

        initListeners()

        downloadNotes()

        observeViewModel()

        initAdapter()

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.intent.send(NotesIntent.CheckAuthorization)
        }
    }

    private fun initListeners() = with(viewBinding) {
        imgOpenProfile.setOnClickListener {
            findNavController().navigate(R.id.action_notes_to_profile)
        }
        imgAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_notes_to_note)
        }

        imgDelete.setOnClickListener {

            lifecycleScope.launch {
                viewModel.intent.send(NotesIntent.DeleteCheckedNotes)
            }

        }

        imgCancel.setOnClickListener {
            lifecycleScope.launch {
                viewModel.intent.send(NotesIntent.CancelCheckedNotes)
            }
        }

        txtSortType.setOnClickListener {
            showSortMenu(it)
        }

        rvNotesSwipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.intent.send(NotesIntent.SynchronizeNotes)
            }

        }

//        rvNotesSwipeRefreshLayout.setOnRefreshListener {
//
//            rvNotesSwipeRefreshLayout.isRefreshing = false
//        }


    }

    private fun showSortMenu(view: View) {
        val pop = PopupMenu(requireContext(), view)
        pop.inflate(R.menu.sort_order)
        pop.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuSortByDateInAscendingOrder -> {
                    lifecycleScope.launch {
                        viewModel.intent.send(NotesIntent.SetSortByDateInAscendingOrder)
                    }
                }
                R.id.menuSortByDateInDescendingOrder -> {
                    lifecycleScope.launch {
                        viewModel.intent.send(NotesIntent.SetSortByDateInDescendingOrder)
                    }
                }
                R.id.menuSortByNoteSizeInAscendingOrder -> {
                    lifecycleScope.launch {
                        viewModel.intent.send(NotesIntent.SetSortByNoteSizeInAscendingOrder)
                    }
                }
                R.id.menuSortByNoteSizeInDescendingOrder -> {
                    lifecycleScope.launch {
                        viewModel.intent.send(NotesIntent.SetSortByNoteSizeInDescendingOrder)
                    }
                }
            }
            true
        }
        pop.show()
    }


    private fun initAdapter() {
        viewBinding.rvNotes.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.rvNotes.adapter = adapter
    }

    private fun downloadNotes() {
        lifecycleScope.launch {
            viewModel.intent.send(NotesIntent.DownloadNotes)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is NotesState.Notes -> setNotesToAdapter(it.list)
                    is NotesState.Authorized -> setAuthorizedProfile(it.regData)
                    is NotesState.NotAuthorized -> setNotAuthorizedProfile()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.event.consumeAsFlow().collect {
                when (it) {
                    is NotesEvent.OpenNote -> openNote(it)
                    is NotesEvent.SetNoteListWithNewVars -> setNotesToAdapter(it.list)
                    is NotesEvent.ShowButtons -> showButtons()
                    is NotesEvent.HideButtons -> hideButtons()
                    is NotesEvent.NotesFromServer -> setNotesFromServerToAdapter(it)
                    is NotesEvent.Error -> toastMessage("Ошибка")
                }
            }
        }

    }

    private fun setNotesFromServerToAdapter(it: NotesEvent.NotesFromServer) {

        if (it.isSynchronized && !it.list.isNullOrEmpty()) {
            adapter.setData(it.list)
            toastMessage("Синхронизировано успешно")
        } else {
            toastMessage("Нет ответа от сервера")
        }

        viewBinding.rvNotesSwipeRefreshLayout.isRefreshing = false
    }

    private fun setNotAuthorizedProfile() = with(viewBinding) {
        println("setNotAuthorizedProfile()")
        txtLogin.text = ""
        imgRedRound.visibility = View.VISIBLE
        rvNotesSwipeRefreshLayout.isEnabled = false
    }

    private fun setAuthorizedProfile(regData: RegData) = with(viewBinding) {
        println("setAuthorizedProfile()")
        txtLogin.text = regData.login
        imgRedRound.visibility = View.GONE
        rvNotesSwipeRefreshLayout.isEnabled = true
    }

    private fun hideButtons() = with(viewBinding) {
        layoutBtnDelete.visibility = View.GONE
    }


    private fun showButtons() = with(viewBinding) {
        layoutBtnDelete.visibility = View.VISIBLE
    }

    private fun openNote(it: NotesEvent.OpenNote) {
        val _id = it.id
        lifecycleScope.launch {
            val bundle = bundleOf(
                noteId to _id
            )
            findNavController().navigate(R.id.action_notes_to_note, bundle)
        }
    }

    private fun setNotesToAdapter(list: List<NoteData>) {
        adapter.setData(list)
    }


    private fun live() {

        viewBinding.imgChangeShowOrder.setOnClickListener {

            val regData = ParseObject("Test")
            regData.put("testId", 12)
            regData.put("header", "test")
            regData.saveInBackground {
                if (it != null) {
                    it.localizedMessage?.let { message -> Log.e("Server error", message) }
                } else {
                    Log.d("Server", "Object saved.")
                }
            }

        }

        val parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient()

        val parseQuery = ParseQuery<ParseObject>("Test")

        val subscriptionHandling = parseLiveQueryClient!!.subscribe(parseQuery)


        subscriptionHandling!!.handleSubscribe {
            subscriptionHandling!!.handleEvent(
                SubscriptionHandling.Event.CREATE
            ) { _: ParseQuery<ParseObject?>?, obj: ParseObject? ->
                println("add ${obj?.getNumber("testId")}")
                println("add ${obj?.getString("header")}")

                //  runOnUiThread { messagesAdapter!!.addItem(`object`) }
            }


            subscriptionHandling!!.handleEvent(
                SubscriptionHandling.Event.DELETE
            ) { _: ParseQuery<ParseObject?>?, obj: ParseObject? ->

                println("delete ${obj?.getNumber("id")}")
                println("delete ${obj?.getString("header")}")

                //  runOnUiThread { messagesAdapter!!.removeItem(`object`!!) }
            }
            subscriptionHandling!!.handleEvent(
                SubscriptionHandling.Event.UPDATE
            ) { _: ParseQuery<ParseObject?>?, obj: ParseObject? ->

                println("update ${obj?.getNumber("id")}")
                println("update ${obj?.getString("header")}")

                //  runOnUiThread { messagesAdapter!!.updateItem(`object`!!) }
            }
        }


    }


}








