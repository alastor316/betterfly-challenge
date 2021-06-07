package com.cadiz.betterfly.register

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cadiz.betterfly.MainActivity
import com.cadiz.betterfly.R
import com.cadiz.betterfly.api.ApiResponseStatus
import com.cadiz.betterfly.character.CharacterListener
import com.cadiz.betterfly.databinding.FragmentRegisterBinding
import com.cadiz.betterfly.model.User
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {

    private var fragmentRegisterBinding: FragmentRegisterBinding? = null
    lateinit var auth: FirebaseAuth
    private lateinit var viewModel: RegisterViewModel
    private lateinit var characterListener: CharacterListener


    override fun onAttach(context: Context) {
        super.onAttach(context)
        characterListener = try {
            context as CharacterListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement CharacterSelectListener")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view :View  = inflater.inflate(R.layout.fragment_register, container, false)
        val binding = FragmentRegisterBinding.bind(view)
        fragmentRegisterBinding = binding
        auth = FirebaseAuth.getInstance()
        VerifyUser()
        loadViewModelProvider()
        setupToolbar()
        register(binding)
        return view
    }


    fun VerifyUser(){
        if(auth.currentUser != null){
            characterListener.NavigateRegisterToListCharacter()
        }
    }

    private fun loadViewModelProvider() {
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    private fun setupToolbar() {
        val toolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.mainToolbar)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.fragment_register)
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }


    private fun register(binding: FragmentRegisterBinding) {

        binding.registerButton.setOnClickListener {
            val isValid :Boolean = validateEditText(binding)
            if(isValid){
                viewModel.register(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString(), binding.firstNameEditText.text.toString() )
                viewModel.authenticatedUserLiveData?.observe(viewLifecycleOwner, Observer {
                    if(it.isNew){
                        createNewUser(it)
                    } else {
                        characterListener.NavigateRegisterToListCharacter()
                    }
                    binding.registerProgressBar.visibility = View.GONE
                })
            }else{
                Toast.makeText(this.requireContext(), getString(R.string.fragment_register_error), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateEditText(binding: FragmentRegisterBinding):Boolean{
        val email = binding.emailEditText.text.toString()
        val firstName = binding.firstNameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(password)

    }




    private fun createNewUser(user: User) {
        viewModel.createUser(user)
        viewModel.createdUserLiveData?.observe(viewLifecycleOwner, Observer {
            if (it.isCreated) {
                Toast.makeText(this.requireContext(), "Registro exitoso!. ", Toast.LENGTH_LONG).show()
            }
            characterListener.NavigateRegisterToListCharacter()
        })
    }


}