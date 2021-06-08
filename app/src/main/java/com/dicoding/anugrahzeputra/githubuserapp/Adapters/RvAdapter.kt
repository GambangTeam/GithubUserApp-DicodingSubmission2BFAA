package com.dicoding.anugrahzeputra.githubuserapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.anugrahzeputra.githubuserapp.ObjectModel.GithubUser
import com.dicoding.anugrahzeputra.githubuserapp.databinding.ItemviewBinding

class RvAdapter(private val listGithubUser: ArrayList<GithubUser>) : RecyclerView.Adapter<RvAdapter.CardViewViewHolder>(
) {
    private lateinit var itemInClick : ItemInClickCallback
    private lateinit var buttonInClick : ItemInClickCallback

    interface ItemInClickCallback {
        fun itemClicked(data: GithubUser)
        fun buttonClicked(data: GithubUser)
    }

    fun setItemInClick(itemInClickCallback: ItemInClickCallback) {
        this.itemInClick = itemInClickCallback
        this.buttonInClick = itemInClickCallback
    }

    inner class CardViewViewHolder(private val binding : ItemviewBinding) : RecyclerView.ViewHolder(binding.root) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val binding = ItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(listGithubUser[position])
    }

    override fun getItemCount(): Int = listGithubUser.size

    /*private lateinit var itemInClick : ItemInClickCallback
    private lateinit var buttonInClick : ItemInClickCallback

    interface ItemInClickCallback {
        fun itemClicked(data: GithubUser)
        fun buttonClicked(data: GithubUser)
    }

    fun setItemInClick(itemInClickCallback: ItemInClickCallback) {
        this.itemInClick = itemInClickCallback
        this.buttonInClick = itemInClickCallback
    }

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_itemPhoto)
        var tvName: TextView = itemView.findViewById(R.id.tv_itemName)
        var tvUserId: TextView = itemView.findViewById(R.id.tv_userId)
        var tvDetail: TextView = itemView.findViewById(R.id.tv_desc)
        var tvDetailFoll: TextView = itemView.findViewById(R.id.tv_desc_foll)
        var btnShare: Button = itemView.findViewById(R.id.btn_share)
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        val users = listGithubUser[position]
        Glide.with(holder.itemView.context)
            .load(users.photo)
            .apply(RequestOptions().override(350, 550))
            .into(holder.imgPhoto)
        holder.tvName.text = users.name
        holder.tvUserId.text = users.id
        val follower = "Follower = " + users.follower
        holder.tvDetail.text = follower
        val following = "Following = " + users.following
        holder.tvDetailFoll.text = following
        holder.itemView.setOnClickListener {
            itemInClick.itemClicked(users)
            Toast.makeText(
                holder.itemView.context,
                "Kamu memilih " + users.name,
                Toast.LENGTH_SHORT
            ).show()
        }
        holder.btnShare.setOnClickListener {
            buttonInClick.buttonClicked(users)
            Toast.makeText(
                holder.itemView.context,
                "Go to Github user " + users.name,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int {
        return listGithubUser.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.itemview, parent, false)
        return CardViewViewHolder(view)
    }*/
}