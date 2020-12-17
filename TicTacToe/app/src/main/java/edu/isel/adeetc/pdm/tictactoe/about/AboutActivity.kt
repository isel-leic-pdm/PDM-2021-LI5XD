package edu.isel.adeetc.pdm.tictactoe.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.databinding.ActivityAboutBinding

/**
 * Screen that displays the author information
 */
class AboutActivity : AppCompatActivity() {

    private val binding: ActivityAboutBinding by lazy {
        ActivityAboutBinding.inflate(layoutInflater)
    }

    /**
     * Callback method that handles the activity initiation
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fun navigateToLinkedIn() {
            val url = Uri.parse(resources.getString(R.string.about_author_linked_in))
            startActivity(Intent(Intent.ACTION_VIEW, url))
        }

        binding.linkedInLogo.setOnClickListener { navigateToLinkedIn() }
        binding.linkedInUrl.setOnClickListener { navigateToLinkedIn() }
    }
}