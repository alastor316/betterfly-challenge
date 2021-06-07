package com.cadiz.betterfly.character.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cadiz.betterfly.databinding.CharacterListItemBinding
import com.cadiz.betterfly.model.Character

class CharacterAdapter: ListAdapter<Character, CharacterAdapter.RepositoryViewHolder>(DiffCallback) {


    companion object DiffCallback : DiffUtil.ItemCallback<Character>(){

        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }


    lateinit var onItemClickListener: (Character) -> Unit


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val binding = CharacterListItemBinding.inflate(LayoutInflater.from(parent.context))
        return RepositoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository : Character = getItem(position)
        holder.bind(repository)

    }


    inner class RepositoryViewHolder(private val binding: CharacterListItemBinding):
            RecyclerView.ViewHolder(binding.root){
        fun bind(character: Character) {

            binding.nameTextview.text = "nombre: "+""+character.name
            binding.statusTextView.text = "estado: "+""+character.status


            Glide.with(itemView.getContext()).load(character.image).circleCrop().into(binding.repositoryImageView);

            binding.root.setOnClickListener {
                if (::onItemClickListener.isInitialized) {
                    onItemClickListener(character)
                }
            }
        }
    }
}