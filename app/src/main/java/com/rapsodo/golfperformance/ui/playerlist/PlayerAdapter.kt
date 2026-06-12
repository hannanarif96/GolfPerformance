package com.rapsodo.golfperformance.ui.playerlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rapsodo.golfperformance.R
import com.rapsodo.golfperformance.databinding.ItemPlayerBinding
import com.rapsodo.golfperformance.domain.model.Player
import com.rapsodo.golfperformance.util.Constants

class PlayerAdapter(
    private val onPlayerClick: (Player) -> Unit
) : ListAdapter<Player, PlayerAdapter.PlayerViewHolder>(PlayerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ItemPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlayerViewHolder(private val binding: ItemPlayerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(player: Player) {
            binding.playerName.text = player.name
            binding.clubName.text = player.clubName
            binding.avgSpeed.text = binding.root.context.getString(R.string.average_speed, player.averageBallSpeed)
            
            val imageUrl = if (player.profileImageUrl.startsWith("http")) {
                player.profileImageUrl
            } else {
                "${Constants.IMAGE_BASE_URL}${player.profileImageUrl}"
            }

            Glide.with(binding.playerImage)
                .load(imageUrl)
                .fitCenter()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(binding.playerImage)

            binding.root.setOnClickListener { onPlayerClick(player) }
        }
    }

    class PlayerDiffCallback : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean = oldItem == newItem
    }
}
