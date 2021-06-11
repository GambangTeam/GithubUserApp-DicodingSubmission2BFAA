package com.dicoding.anugrahzeputra.githubuserapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.anugrahzeputra.githubuserapp.objectmodels.GithubUser
import com.dicoding.anugrahzeputra.githubuserapp.databinding.ItemviewBinding

class RvAdapter(private val listGithubUser: ArrayList<GithubUser>) : RecyclerView.Adapter<RvAdapter.CardViewViewHolder>(
) {
    private lateinit var itemInClick: ItemInClickCallback
    private lateinit var buttonInClick: ItemInClickCallback

    interface ItemInClickCallback {
        fun itemClicked(data: GithubUser)
        fun buttonClicked(data: GithubUser)
    }

    fun setItemInClick(itemInClickCallback: ItemInClickCallback) {
        this.itemInClick = itemInClickCallback
        this.buttonInClick = itemInClickCallback
    }

    inner class CardViewViewHolder(private val binding: ItemviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(users: GithubUser) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(users.photo)
                    .apply(RequestOptions().override(350, 550))
                    .into(imgItemPhoto)
                tvItemName.text = users.name
                tvUserId.text = users.id
                val follower = "Follower = " + users.follower
                tvDesc.text = follower
                val following = "Following = " + users.following
                tvDescFoll.text = following
                itemView.setOnClickListener {
                    itemInClick.itemClicked(users)
                    Toast.makeText(
                        itemView.context,
                        "Kamu memilih " + users.name,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                btnShare.setOnClickListener {
                    buttonInClick.buttonClicked(users)
                    Toast.makeText(
                        itemView.context,
                        "Go to Github user " + users.name,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    class FollowersCardViewViewHolder {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val binding = ItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(listGithubUser[position])
    }

    override fun getItemCount(): Int = listGithubUser.size
}
