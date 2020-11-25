package edu.isel.pdm.memorymatrix.game.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import edu.isel.pdm.memorymatrix.MemoryMatrixApplication
import edu.isel.pdm.memorymatrix.utils.confinedLazy

/**
 * View model fot the activity that displays the game's score history.
 */
class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Asynchronously gets all the existing scores.
     *
     * Implementation note: This eager approach (getting all the results) is only acceptable for
     * demonstration purposes.
     */
    val results: LiveData<List<GameResult>> by confinedLazy {
        getApplication<MemoryMatrixApplication>().gameRepository.getAllScores()
    }
}
