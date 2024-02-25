package com.example.optimizingcode.ui

import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.optimizingcode.R
import com.example.optimizingcode.adapters.NoteAdapter
import com.example.optimizingcode.data.entity.Note
import com.example.optimizingcode.databinding.DeletePopupBinding
import com.example.optimizingcode.databinding.FragmentNotesBinding
import com.example.optimizingcode.viewModel.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteFragment: Fragment(R.layout.fragment_notes), NoteAdapter.OnNoteClickListener {

    private val viewModel by viewModels<NoteViewModel>()
    private  lateinit var  binding: FragmentNotesBinding
     private  val myAdapter:NoteAdapter by lazy { NoteAdapter(this@NoteFragment) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNotesBinding.bind(requireView())

        binding.apply {
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL )
            recyclerView.setHasFixedSize(false)

            floatingActionBtn.setOnClickListener(){
                    val action = NoteFragmentDirections.actionNoteFragment2ToAddEditNoteFragment2(null)
                    findNavController().navigate(action)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.notes.collect{notes->
                    Log.d("NoteSize57",notes.size.toString())
//                    myAdapter = NoteAdapter(this@NoteFragment)
                    myAdapter.notifyDataSetChanged()
                    recyclerView.adapter=myAdapter

                    myAdapter.setData(notes)
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.notesEvent.collect{event->

                    if(event is NoteViewModel.NotesEvents.ShowUndoSnackBar){
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).setAction("Undo"){
                            viewModel.insertNote(event.note)
                        }.show()
                    }

                }
            }

            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // This method is called before the text in the EditText is changed
                }

                override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
                    // This method is called when the text in the EditText is changed
                    val newText = query.toString()
//                    searchDatabase(newText)
//                    searchDatabase(newText)
//                    Toast.makeText(context, "Text changed: $newText", Toast.LENGTH_SHORT).show()


                }

                override fun afterTextChanged(s: Editable?) {
                    val newText = s.toString()
//                    searchDatabase(newText)
                    // This method is called after the text in the EditText has been changed
//                    val newText = s.toString()
//                    searchDatabase(newText)
//                    Toast.makeText(context, "Text changed: $newText", Toast.LENGTH_SHORT).show()
                }
            }

//            searchEdt.addTextChangedListener(textWatcher)

            searchEdt.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    event != null && event.action == KeyEvent.ACTION_DOWN &&
                    event.keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    // Perform your search function here
                    performSearch()
                    return@setOnEditorActionListener true
                }
                false
            }

        }

    }

    private fun performSearch() {
        // Replace this with your search functionality
        val query = binding.searchEdt.text.toString()
        val searchQuery = "%$query%"

        viewModel.searchDatabase(searchQuery)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner){ list ->
                list.let {

                    myAdapter.setData(list)
//                  myAdapter = NoteAdapter( this@NoteFragment)
//                    binding.recyclerView.adapter = myAdapter
////                    myAdapter.notifyDataSetChanged()
//                    Log.d("NoteSize131",list.size.toString())

                }

            }
        }




    }
    private fun searchDatabase(query:String){
        val searchQuery = "%$query%"

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchDatabase(searchQuery).observe(this@NoteFragment){ list ->
                list.let {


                    val adapter = NoteAdapter( this@NoteFragment)
                    binding.recyclerView.adapter = adapter

                    Log.d("checkingData",list.toString())

                }

            }
        }



    }



    override fun onNoteClick(note: Note) {
        val action = NoteFragmentDirections.actionNoteFragment2ToAddEditNoteFragment2(note)
        findNavController().navigate(action)
    }

    override fun onNoteLongClick(note: Note) {


        val view =
            LayoutInflater.from(context).inflate(R.layout.delete_popup, null)
        val builder =
            AlertDialog.Builder(requireContext(), R.style.dialog_transparent_style)
                .setView(view)

        val dialogBinding = DeletePopupBinding.bind(view)
        val mAlertDialog = builder.show()

        val window = mAlertDialog.window
        val params = window?.attributes
        params?.width = WindowManager.LayoutParams.WRAP_CONTENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = params

        window!!.setGravity(Gravity.CENTER)
        window.setBackgroundDrawableResource(R.color.transparent)


        dialogBinding.title.text =
            "Are you sure you want to delete this note?"

        dialogBinding.deleteBtn.setOnClickListener {
            viewModel.deleteNote(note)
            mAlertDialog.dismiss()
        }
        dialogBinding.cancleBtn.setOnClickListener {
            mAlertDialog.dismiss()
        }

        builder.setCancelable(true)






    }


}