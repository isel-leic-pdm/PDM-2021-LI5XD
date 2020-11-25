package edu.isel.pdm.memorymatrix.game.history

import android.os.Bundle
import edu.isel.pdm.memorymatrix.databinding.ActivityHistoryBinding
import edu.isel.pdm.memorymatrix.utils.BaseActivity
import edu.isel.pdm.memorymatrix.utils.confinedLazy

/**
 * Screen used to display the game scores' history using a Recycler View.
 */
class HistoryActivity : BaseActivity() {

    private val binding: ActivityHistoryBinding by confinedLazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }

    private val gameRepository by confinedLazy { memoryMatrixApp.gameRepository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}