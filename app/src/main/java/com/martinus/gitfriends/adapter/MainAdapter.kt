package com.martinus.gitfriends.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.martinus.gitfriends.databinding.ItemUsersBinding
import com.martinus.gitfriends.repository.remote.response.GithubUser
import com.martinus.gitfriends.ui.DetailActivity

class MainAdapter(private val listUsers: List<GithubUser>) :
    RecyclerView.Adapter<MainAdapter.FollowersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
        return FollowersViewHolder(
            ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        val (avatarUrl, username) = listUsers[position]
        holder.binding.tvItemName.text = username
        Glide.with(holder.itemView.context)
            .load(avatarUrl)
            .into(holder.binding.imgItemPhoto)


        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.data, username)
            intent.putExtra(DetailActivity.img, avatarUrl)
            Toast.makeText(holder.itemView.context, username, Toast.LENGTH_SHORT)
                .show()
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }

    class FollowersViewHolder(var binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root)

}