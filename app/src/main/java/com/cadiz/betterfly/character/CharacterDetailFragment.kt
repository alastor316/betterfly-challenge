package com.cadiz.betterfly.character

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cadiz.betterfly.MainActivity
import com.cadiz.betterfly.R
import com.cadiz.betterfly.databinding.FragmentCharacterDetailBinding
import com.cadiz.betterfly.model.Character


class CharacterDetailFragment : Fragment() {

    val args: CharacterDetailFragmentArgs by navArgs()

    private var fragmentCharacterDetailBinding: FragmentCharacterDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_character_detail, container, false)

        val character = args.character


        val binding = FragmentCharacterDetailBinding.bind(view)
        fragmentCharacterDetailBinding = binding

        setupToolbar(character)


        setCharacterData(character, binding)
        return view
    }

    private fun setCharacterData(character: Character, binding: FragmentCharacterDetailBinding) {

        binding.loadingProgressBar.visibility = View.VISIBLE
        Glide.with(this).load(character.image).listener(object: RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                binding.loadingProgressBar.visibility = View.GONE
                binding.characterImageView.setImageResource(R.drawable.ic_error_url)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                binding.loadingProgressBar.visibility = View.GONE
                return false
            }
        }).error(R.drawable.ic_error_url).into(binding.characterImageView)

    //    binding.nameTextView.text = character.name

        binding.statusTextView.text = "Estado: "+character.status
        binding.speciesTextView.text = "Especie: "+character.species
        binding.genderTextView.text = "Genero: "+character.gender


    }

    private fun setupToolbar(character: Character) {
        val toolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.mainToolbar)
        toolbar.title = character.name
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}