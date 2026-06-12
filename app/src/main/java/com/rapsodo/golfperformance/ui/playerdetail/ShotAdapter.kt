package com.rapsodo.golfperformance.ui.playerdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rapsodo.golfperformance.R
import com.rapsodo.golfperformance.databinding.ItemShotBinding
import com.rapsodo.golfperformance.domain.model.Shot

class ShotAdapter : PagingDataAdapter<Shot, ShotAdapter.ShotViewHolder>(ShotDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShotViewHolder {
        val binding = ItemShotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShotViewHolder, position: Int) {
        val shot = getItem(position)
        if (shot != null) {
            holder.bind(shot)
            setAnimation(holder.itemView, position)
        }
    }

    private var lastPosition = -1
    private fun setAnimation(viewToAnimate: android.view.View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.item_animation_fall_down)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    class ShotViewHolder(private val binding: ItemShotBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(shot: Shot) {
            binding.shotInfo.text = "Speed: ${shot.ballSpeed} mph | Distance: ${shot.carryDistance} yds"
            binding.shotSubInfo.text = "Angle: ${shot.launchAngle}° | Spin: ${shot.spinRate} rpm | Club: ${shot.clubType}"
        }
    }

    class ShotDiffCallback : DiffUtil.ItemCallback<Shot>() {
        override fun areItemsTheSame(oldItem: Shot, newItem: Shot): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Shot, newItem: Shot): Boolean = oldItem == newItem
    }
}
