package com.omnilog.eurosport.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.omnilog.eurosport.databinding.PlayerActivityBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding : PlayerActivityBinding
    private lateinit var player: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlayerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onResume() {
        super.onResume()

        val url = intent.getStringExtra(EXTRA_URL) ?: throw IllegalStateException("To start player you must provide the url of the video")

        player = SimpleExoPlayer.Builder(this).build()
        player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        binding.styledPlayer.player = player

        // Build the media item.
        val mediaItem: MediaItem = MediaItem.fromUri(url)
        // Set the media item to be played.
        player.setMediaItem(mediaItem)
        // Prepare the player.
        player.prepare()
        // Start the playback.
        player.play()
    }

    override fun onPause() {
        player.stop()
        player.release()
        super.onPause()
    }

    companion object {
        const val EXTRA_URL = "EXTRA_URL"
    }
}