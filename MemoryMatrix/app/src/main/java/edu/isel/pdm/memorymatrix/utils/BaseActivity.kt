package edu.isel.pdm.memorymatrix.utils

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import edu.isel.pdm.memorymatrix.MemoryMatrixApplication

/**
 * Base class for all the application's activities, used for logging.
 */
open class BaseActivity : AppCompatActivity() {

    private val appTag by lazy { (application as MemoryMatrixApplication).appTag }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(appTag, "onCreate() on $localClassName")
    }

    override fun onStart() {
        super.onStart()
        Log.v(appTag, "onStart() on $localClassName")
    }

    override fun onResume() {
        super.onResume()
        Log.v(appTag, "onResume() on $localClassName")
    }

    override fun onPause() {
        super.onPause()
        Log.v(appTag, "onPause() on $localClassName")
    }

    override fun onStop() {
        super.onStop()
        Log.v(appTag, "onStop() on $localClassName")
    }

    override fun onRestart() {
        super.onRestart()
        Log.v(appTag, "onRestart() on $localClassName")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(appTag, "onDestroy() on $localClassName")
    }
}
