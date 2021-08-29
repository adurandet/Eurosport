package com.omnilog.eurosport.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.omnilog.eurosport.R
import com.omnilog.eurosport.databinding.HomeItemStoryBinding
import com.omnilog.eurosport.databinding.HomeItemVideoBinding
import com.omnilog.eurosport.home.model.HomeContentType
import com.omnilog.eurosport.home.model.IHomeItemContent
import com.omnilog.eurosport.home.model.Story
import com.omnilog.eurosport.home.model.Video
import com.omnilog.eurosport.utils.toDateTime

class ContentListAdapter(
    private val onVideoClickListener: OnVideoClickListener,
    private val onStoryClickListener: OnStoryClickListener
) :
    ListAdapter<IHomeItemContent, ContentViewHolder>(ContentDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeContentType.values()[viewType].createViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        when (holder) {
            is StoryViewHolder -> {
                holder.bind(getItem(position) as Story, onStoryClickListener)
            }
            is VideoViewHolder -> {
                holder.bind(getItem(position) as Video, onVideoClickListener)
            }
        }
    }
}

abstract class ContentViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView)

class StoryViewHolder(private val binding: HomeItemStoryBinding) : ContentViewHolder(binding.root) {
    fun bind(storyData: Story, onStoryClickListener: OnStoryClickListener) {
        with(binding) {
            title.text = storyData.title
            Glide.with(root.context)
                .load(storyData.image)
                .fitCenter()
                .into(image)
            sportLabel.sportLabelText.text = storyData.sportName
            authorAndTime.text =
                root.context.getString(R.string.authorAndTime, storyData.author, storyData.date.toDateTime(root.context))

            root.setOnClickListener {
                onStoryClickListener.onStoryClickListener(storyData)
            }
        }
    }
}

class VideoViewHolder(private val binding: HomeItemVideoBinding) : ContentViewHolder(binding.root) {
    fun bind(videoData: Video, onVideoClickListener: OnVideoClickListener) {
        with(binding) {
            title.text = videoData.title
            Glide.with(root.context)
                .load(videoData.thumb)
                .fitCenter()
                .into(image)
            sportLabel.sportLabelText.text = videoData.sportName
            views.text = root.context.getString(R.string.views, videoData.views)

            root.setOnClickListener {
                onVideoClickListener.onVideoClickListener(videoData.url)
            }
        }
    }
}

private class ContentDiffCallback : DiffUtil.ItemCallback<IHomeItemContent>() {
    override fun areItemsTheSame(oldItem: IHomeItemContent, newItem: IHomeItemContent) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: IHomeItemContent, newItem: IHomeItemContent) =
        oldItem.id == newItem.id
}