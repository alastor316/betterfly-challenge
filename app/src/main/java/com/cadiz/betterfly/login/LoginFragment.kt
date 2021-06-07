package com.cadiz.betterfly.login

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cadiz.betterfly.MainActivity
import com.cadiz.betterfly.R
import com.cadiz.betterfly.character.CharacterListener
import com.cadiz.betterfly.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import java.lang.ClassCastException



class LoginFragment : Fragment() {

    private lateinit var characterLisetner: CharacterListener
    private var fragmentLoginBinding: FragmentLoginBinding? = null

    lateinit var auth: FirebaseAuth
    private lateinit var viewModel: LoginViewModel



    override fun onAttach(context: Context) {
        super.onAttach(context)
        characterLisetner = try {
            context as CharacterListener
        } catch (e: ClassCastException){
            throw ClassCastException("$context must implement CharacterSelectListener")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view :View  = inflater.inflate(R.layout.fragment_login, container, false)
        val binding = FragmentLoginBinding.bind(view)
        fragmentLoginBinding = binding
        auth = FirebaseAuth.getInstance()
        VerifyUser()
        setupToolbar()
        loadViewModelProvider()

      //  loadAuth()
        login(binding)


        binding.registerTexView.setOnClickListener{
            characterLisetner.NavigateLoginToRegister()
        }

        return view
    }

    fun VerifyUser(){
        if(auth.currentUser != null){
            characterLisetner.NavigateLoginToListCharacter()
        }
    }

    private fun loadViewModelProvider() {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }


    private fun login(binding: FragmentLoginBinding) {

        binding.loginButton.setOnClickListener {

            val isValid :Boolean = validateEditText(binding)
            if(isValid) {
                viewModel.login(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
                viewModel.loginUserLiveData?.observe(viewLifecycleOwner, Observer {
                        //  createNewUser(it)
                        characterLisetner.NavigateLoginToListCharacter()
                })
            }else{
                Toast.makeText(this.requireContext(), "Error campos vacios o formato correo incorrecto", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateEditText(binding: FragmentLoginBinding):Boolean{
        val email = binding.emailEditText.text.toString()
        val checkEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val password = binding.passwordEditText.text.toString()

        if( checkEmail && !TextUtils.isEmpty(password)){
            return true
        }else{
            return false
        }

    }


        private fun setupToolbar() {
        val toolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.mainToolbar)
        toolbar.navigationIcon = null
        toolbar.title = getString(R.string.fragment_login)
        toolbar.setTitleTextColor(Color.WHITE)
    }

}