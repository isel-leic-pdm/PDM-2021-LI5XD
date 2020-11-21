package edu.isel.pdm.memorymatrix

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import edu.isel.pdm.memorymatrix.databinding.ActivityLevelBinding
import edu.isel.pdm.memorymatrix.game.GameActivity
import edu.isel.pdm.memorymatrix.game.PATTERN_SIZE_EXTRA
import kotlin.math.max


private fun SeekBar.setChangeListener(listener: (progress: Int) -> Unit) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            if (fromUser) listener.invoke(progress)
        }
        override fun onStartTrackingTouch(seekBar: SeekBar) { }
        override fun onStopTrackingTouch(seekBar: SeekBar) { }
    })
}

/**
 * Screen used for level selection.
 */
class LevelActivity : AppCompatActivity() {

    private val binding: ActivityLevelBinding by lazy { ActivityLevelBinding.inflate(layoutInflater) }
    private val minLevel by lazy { resources.getInteger(R.integer.min_level) }
    private val gameRepository by lazy { (application as MemoryMatrixApplication).gameRepository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.levelSelectionBar.setChangeListener {
            val level = max(minLevel, it)
            binding.levelSelectionBar.progress = level
            binding.levelSelection.text = level.toString()
        }

        binding.playButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra(PATTERN_SIZE_EXTRA, binding.levelSelectionBar.progress)
            }
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.record.text = gameRepository.highestLevel.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_level, menu)
        menu.findItem(R.id.history).setOnMenuItemClickListener {
            true
        }
        return true
    }
}
