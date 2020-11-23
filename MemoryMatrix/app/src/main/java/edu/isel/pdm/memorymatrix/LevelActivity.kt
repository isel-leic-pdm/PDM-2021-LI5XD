package edu.isel.pdm.memorymatrix

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SeekBar
import edu.isel.pdm.memorymatrix.databinding.ActivityLevelBinding
import edu.isel.pdm.memorymatrix.game.GameActivity
import edu.isel.pdm.memorymatrix.game.PATTERN_SIZE_EXTRA
import edu.isel.pdm.memorymatrix.utils.BaseActivity
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

    private val binding: ActivityLevelBinding by lazy { ActivityLevelBinding.inflate(layoutInflater) }
    private val minLevel by lazy { resources.getInteger(R.integer.min_level) }
    private val gameRepository by lazy { memoryMatrixApp.gameRepository }

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
        Log.v(memoryMatrixApp.appTag, "progress is ${binding.levelSelectionBar.progress}")
        binding.levelSelection.text = binding.levelSelectionBar.progress.toString()
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_level, menu)
        menu.findItem(R.id.history).setOnMenuItemClickListener {
            gameRepository.getAllScores().observe(this) {
                // TODO: Populate the UI
                Log.v(memoryMatrixApp.appTag, "Populating the UI in thread ${Thread.currentThread().name}")
            }
            true
        }
        return true
    }
}
