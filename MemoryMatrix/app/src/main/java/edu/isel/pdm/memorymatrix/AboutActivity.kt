package edu.isel.pdm.memorymatrix

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import edu.isel.pdm.memorymatrix.databinding.ActivityAboutBinding
import edu.isel.pdm.memorymatrix.utils.BaseActivity
import edu.isel.pdm.memorymatrix.utils.confinedLazy
import edu.isel.pdm.memorymatrix.utils.runDelayed

private const val LUMOSITY_URL = "https://www.lumosity.com/"
private const val MY_YOUTUBE_CHANNEL = "https://www.youtube.com/channel/UCetmdF6qGnMAdZP32i8AnbA"
private const val MY_TWITCH_CHANNEL = "https://www.twitch.tv/paulo_pereira"
private const val MY_LINKED_IN = "https://www.linkedin.com/in/palbp/"

/**
 * Extension method to parse the string as a URI
 */
private fun String.parseUri() = Uri.parse(this)

/**
 * Splash screen displayed when the application starts
 */
class AboutActivity : BaseActivity() {

    private fun tryNavigateTo(destinationUri: Uri, navigationSource: View) {
        val intent = Intent(Intent.ACTION_VIEW, destinationUri)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            navigationSource.setOnClickListener(null)
        }
    }

    private val binding by confinedLazy { ActivityAboutBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.imageLumosity.setOnClickListener {
            tryNavigateTo(LUMOSITY_URL.parseUri(), binding.imageLumosity)
        }
        binding.imageYouTube.setOnClickListener {
            tryNavigateTo(MY_YOUTUBE_CHANNEL.parseUri(), binding.imageYouTube)
        }
        binding.imageTwitch.setOnClickListener {
            tryNavigateTo(MY_TWITCH_CHANNEL.parseUri(), binding.imageTwitch)
        }
        binding.imageLinkedIn.setOnClickListener {
            tryNavigateTo(MY_LINKED_IN.parseUri(), binding.imageLinkedIn)
        }
    }
}