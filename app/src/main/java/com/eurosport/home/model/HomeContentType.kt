package com.eurosport.home.model

import android.view.LayoutInflater
import android.view.ViewGroup
import com.eurosport.databinding.HomeItemStoryBinding
import com.eurosport.databinding.HomeItemVideoBinding
import com.eurosport.home.ContentViewHolder
import com.eurosport.home.StoryViewHolder
import com.eurosport.home.VideoViewHolder

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