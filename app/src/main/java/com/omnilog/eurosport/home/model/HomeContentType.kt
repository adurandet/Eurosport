package com.omnilog.eurosport.home.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.omnilog.eurosport.databinding.HomeItemStoryBinding
import com.omnilog.eurosport.databinding.HomeItemVideoBinding
import com.omnilog.eurosport.home.ContentViewHolder
import com.omnilog.eurosport.home.StoryViewHolder
import com.omnilog.eurosport.home.VideoViewHolder

enum class HomeContentType {

    STORY {
        override fun createViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            attachToRoot: Boolean
        ): StoryViewHolder {
            val binding = HomeItemStoryBinding.inflate(
                inflater,
                parent,
                attachToRoot
            )
            return StoryViewHolder(binding)
        }
    },
    VIDEO {
        override fun createViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            attachToRoot: Boolean
        ): VideoViewHolder {
            val binding = HomeItemVideoBinding.inflate(
                inflater,
                parent,
                attachToRoot
            )
            return VideoViewHolder(binding)
        }
    };

    abstract fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        attachToRoot: Boolean = false
    ): ContentViewHolder
}