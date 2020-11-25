package edu.isel.pdm.memorymatrix

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.SeekBar
import edu.isel.pdm.memorymatrix.databinding.ActivityLevelBinding
import edu.isel.pdm.memorymatrix.game.GameActivity
import edu.isel.pdm.memorymatrix.game.PATTERN_SIZE_EXTRA
import edu.isel.pdm.memorymatrix.game.history.HistoryActivityDemo
import edu.isel.pdm.memorymatrix.utils.BaseActivity
import edu.isel.pdm.memorymatrix.utils.confinedLazy
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
class LevelActivity : BaseActivity() {

    private val binding by confinedLazy { ActivityLevelBinding.inflate(layoutInflater) }
    private val minLevel by confinedLazy { resources.getInteger(R.integer.min_level) }
    private val gameRepository by confinedLazy { memoryMatrixApp.gameRepository }

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

    override fun onResume() {
        super.onResume()
        binding.record.text = gameRepository.highestLevel.toString()
        binding.levelSelection.text = binding.levelSelectionBar.progress.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_level, menu)
        menu.findItem(R.id.historyList).setOnMenuItemClickListener {
            startActivity(Intent(this, HistoryActivityDemo::class.java))
            //startActivity(Intent(this, HistoryActivity::class.java))
            true
        }
        return true
    }
}
