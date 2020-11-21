package edu.isel.pdm.memorymatrix

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.isel.pdm.memorymatrix.utils.BaseActivity
import edu.isel.pdm.memorymatrix.utils.runDelayed

class SplashViewModel : ViewModel() {

    val scheduleComplete: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    private var isScheduled = false

    fun scheduleTransition(millis: Long) {
        if (!isScheduled) {
            isScheduled = true
            runDelayed(millis) {
                scheduleComplete.value = true
            }
        }
    }
}

/**
 * Splash screen displayed when the application starts
 */
class SplashActivity : BaseActivity() {

    private val contentView by lazy { findViewById<View>(R.id.root) }
    private val viewModel: SplashViewModel by viewModels()

    private fun navigateToLevelSelectionActivity() {
        startActivity(Intent(this, LevelActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        contentView.setOnClickListener {
            navigateToLevelSelectionActivity()
        }

        viewModel.scheduleComplete.observe(this) { shouldNavigate ->
            if (shouldNavigate) {
                navigateToLevelSelectionActivity()
            }
        }

        viewModel.scheduleTransition(30000)
    }
}








