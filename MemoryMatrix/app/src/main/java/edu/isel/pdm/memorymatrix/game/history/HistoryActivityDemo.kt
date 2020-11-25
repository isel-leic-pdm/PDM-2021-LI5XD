package edu.isel.pdm.memorymatrix.game.history

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.viewModels
import edu.isel.pdm.memorymatrix.MemoryMatrixApplication
import edu.isel.pdm.memorymatrix.R
import edu.isel.pdm.memorymatrix.utils.BaseActivity
import edu.isel.pdm.memorymatrix.utils.confinedLazy

/**
 * Adapter used to fill the list view items with the information from the data source.
 */
class HistoryAdapter(ctx: Context, history: Array<GameResult>) :
    ArrayAdapter<GameResult>(ctx, android.R.layout.simple_list_item_2, history) {

    private val application by confinedLazy { context.applicationContext as MemoryMatrixApplication }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        Log.v(application.appTag, "converting view? ${convertView != null}")
        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)

        val gameResult = getItem(position) ?: throw IllegalStateException()

        itemView.findViewById<TextView>(android.R.id.text1).text =
            context.resources.getString(R.string.history_item_score, gameResult.score.toString())
        itemView.findViewById<TextView>(android.R.id.text2).text = gameResult.date.toString()

        return itemView
    }
}

/**
 * Screen used to display the game scores' history using a List View.
 */
class HistoryActivityDemo : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_demo)

        val history = findViewById<ListView>(R.id.historyList)
        val viewModel: HistoryViewModel by viewModels()

        viewModel.results.observe(this) {
            history.adapter = HistoryAdapter(this, it.toTypedArray())
        }
    }
}