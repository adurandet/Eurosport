package com.omnilog.eurosport.story

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.omnilog.eurosport.R
import com.omnilog.eurosport.databinding.StoryDetailFragmentBinding
import com.omnilog.eurosport.utils.toDateTime

class StoryDetailFragment : Fragment() {

    private val storyArgs: StoryDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val storyData = storyArgs.story

        val binding = StoryDetailFragmentBinding.inflate(inflater)

        val navController = findNavController()
        with(binding) {
            toolbar.setupWithNavController(navController)
            toolbar.setNavigationIcon(R.drawable.back)
            toolbar.inflateMenu(R.menu.story_detail_menu)
            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.share -> Snackbar.make(
                        root,
                        getString(R.string.share_id, storyData.id),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                true
            }

            Glide.with(root.context)
                .load(storyData.image)
                .fitCenter()
                .into(image)
            sportLabel.sportLabelText.text = storyData.sportName
            title.text = storyData.title
            val byAuthor = getString(R.string.byAuthor, storyData.author)
            val spannableString = SpannableString(byAuthor).apply {
                setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.blue_500)),
                    byAuthor.length - storyData.author.length,
                    byAuthor.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            author.text = spannableString

            time.text = storyData.date.toDateTime(requireContext())
            teaser.text = storyData.teaser
        }

        return binding.root
    }
}