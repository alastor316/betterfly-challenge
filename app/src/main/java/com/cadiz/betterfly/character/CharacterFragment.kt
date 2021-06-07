package com.cadiz.betterfly.character

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cadiz.betterfly.MainActivity
import com.cadiz.betterfly.R
import com.cadiz.betterfly.api.ApiResponseStatus
import com.cadiz.betterfly.character.adapter.CharacterAdapter
import com.cadiz.betterfly.databinding.FragmentCharacterBinding
import com.cadiz.betterfly.model.Character
import com.google.firebase.auth.FirebaseAuth


class CharacterFragment : Fragment() {

    private lateinit var viewModel: CharacterViewModel
    private lateinit var characterListener: CharacterListener
    private var fragmentCharacterBinding: FragmentCharacterBinding? = null
    lateinit var auth: FirebaseAuth


    override fun onAttach(context: Context) {
        super.onAttach(context)
        characterListener = try {
            context as CharacterListener
        } catch (e: ClassCastException){
            throw ClassCastException("$context must implement RepositorySelectListener")
        }
    }



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view :View  =  inflater.inflate(R.layout.fragment_character, container, false)
        val binding = FragmentCharacterBinding.bind(view)
        fragmentCharacterBinding = binding
        auth = FirebaseAuth.getInstance()

        setupToolbar()

        setHasOptionsMenu(true);


        val adapter =  loadRecyclerView(binding)

        adapter.onItemClickListener = {
            characterListener.NavigateListCharacterToCharacterDetail(it)
        }

        loadViewModelProvider()
        loadCharacterList(adapter)
        loadProgressBar(binding)
        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId :Int = item.itemId
        if(itemId == R.id.main_logout) {
            logoutDialog()
        }
        return super.onOptionsItemSelected(item)
    }



    private fun loadRecyclerView(binding: FragmentCharacterBinding): CharacterAdapter {
        binding.characterRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = CharacterAdapter()
        binding.characterRecyclerView.adapter = adapter
        return adapter;
    }


    private fun loadViewModelProvider() {
        viewModel = ViewModelProvider(this,
                CharacterViewModelFactory(
                        requireActivity().application
                )
        ).get(CharacterViewModel::class.java)
    }


    private fun loadCharacterList(adapter: CharacterAdapter) {
        viewModel.chararcterList.observe(viewLifecycleOwner, Observer {
            repoList: MutableList<Character> ->
            adapter.submitList(repoList)
        })
    }


    private fun setupToolbar() {
        val toolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.mainToolbar)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.fragment_character_title)
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.navigationIcon = null
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }



    private fun loadProgressBar(binding: FragmentCharacterBinding){

        viewModel.status.observe(viewLifecycleOwner, Observer {
            apiresponseStatus: ApiResponseStatus ->
            if(apiresponseStatus == ApiResponseStatus.LOADING){
                binding.characterProgressBar.visibility = View.VISIBLE
            } else if(apiresponseStatus == ApiResponseStatus.DONE){
                binding.characterProgressBar.visibility = View.GONE
            }
            else if(apiresponseStatus == ApiResponseStatus.ERROR){
                binding.characterProgressBar.visibility = View.GONE
            }
        })
    }

    private fun logoutDialog(){
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setTitle("¿Desea cerrar la apliación?")
        //    builder.setMessage("mensaje")
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            auth.signOut()
            (activity as MainActivity).finish()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->

        }

        builder.show()
    }



}