package edu.isel.pdm.memorymatrix

import android.content.Intent
import android.os.Bundle
import android.view.View
import edu.isel.pdm.memorymatrix.game.GameActivity
import edu.isel.pdm.memorymatrix.utils.BaseActivity

/**
 * Splash screen displayed when the application starts
 */
class AboutActivity : BaseActivity() {

    private val contentView by lazy { findViewById<View>(R.id.root) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        contentView.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
    }
}