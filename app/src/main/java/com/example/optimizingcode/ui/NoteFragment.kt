package com.example.optimizingcode.ui

import android.app.Activity

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.dolatkia.animatedThemeManager.ThemeManager
import com.example.optimizingcode.DarkTheme
import com.example.optimizingcode.LightTheme

import com.example.optimizingcode.MyAppTheme
import com.example.optimizingcode.R
import com.example.optimizingcode.Utils.AppInfo
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
class NoteFragment: ThemeFragment()  , NoteAdapter.OnNoteClickListener {

    var switch =0


    private val viewModel by viewModels<NoteViewModel>()
    private  lateinit var  binding: FragmentNotesBinding
     private  val myAdapter:NoteAdapter by lazy { NoteAdapter(this@NoteFragment) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        switch = if(AppInfo.getGetDarkMode()){
            1;
        }else {
            0;
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes, container, false)

        binding.apply {
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL )
            recyclerView.setHasFixedSize(false)

            floatingActionBtn.setOnClickListener(){
                    val action = NoteFragmentDirections.actionNoteFragmentToAddEditNoteFragment(null)
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
                    performSearch()
//                    Toast.makeText(context, "Text changed: $newText", Toast.LENGTH_SHORT).show()


                }

                override fun afterTextChanged(s: Editable?) {
                    val newText = s.toString()
//                    searchDatabase(newText)
                    // This method is called after the text in the EditText has been changed
//                    val newText = s.toString()
//                    searchDatabase(newText)
//                    performSearch()
//                    Toast.makeText(context, "Text changed: $newText", Toast.LENGTH_SHORT).show()
                }
            }

            searchEdt.addTextChangedListener(textWatcher)

            searchEdt.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    event != null && event.action == KeyEvent.ACTION_DOWN &&
                    event.keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    hideKeyboard(requireActivity())
                    // Perform your search function here
//                    performSearch()
                    return@setOnEditorActionListener true
                }
                false
            }



            imageView.setOnClickListener(){
                if(switch==0){
                    switch=1
                    ThemeManager.instance.changeTheme(DarkTheme(),it,2300)
                    AppInfo.setDarkMode(true)
                }else{
                    switch=0
                    AppInfo.setDarkMode(false)
                }
                ThemeManager.instance.reverseChangeTheme(LightTheme(),it,2300)
//                binding.text.playAnimation()
            }

        }

        return binding.root

    }

      override fun syncTheme(appTheme: AppTheme) {

       val myAppTheme = appTheme as MyAppTheme

           binding.apply {
               background.setBackgroundColor(myAppTheme.backgroundColor(requireContext()))
               searchEdt.setTextColor(myAppTheme.mainTextColor(requireContext()))
               imageView.setColorFilter(myAppTheme.changeIconColor(requireContext()))
               heading.setTextColor(myAppTheme.mainTextColor(requireContext()))
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



    // this is used for navigation from one fragment to another fragment
    override fun onNoteClick(note: Note) {
        val action = NoteFragmentDirections.actionNoteFragmentToAddEditNoteFragment(note)
        findNavController().navigate(action)
    }

    // pop up funciton
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

    // keyboard hiding feature when we click on search button
    private fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }




}