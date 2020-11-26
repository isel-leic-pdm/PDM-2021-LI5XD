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

class ItemViewHolderLegacy(itemView: View) {
    val topText: TextView = itemView.findViewById(android.R.id.text1)
    val bottomText: TextView = itemView.findViewById(android.R.id.text2)
}

/**
 * Adapter used to fill the list view items with the information from the data source.
 */
class HistoryAdapterLegacy(ctx: Context, history: List<GameResult>) :
    ArrayAdapter<GameResult>(ctx, android.R.layout.simple_list_item_2, history.toTypedArray()) {

    private val application = context.applicationContext as MemoryMatrixApplication
    private val topTextPlaceholder: String = ctx.resources.getString(R.string.history_item_score)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        Log.v(application.appTag, "converting view? ${convertView != null}")

        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)

        val gameResult = getItem(position) ?: throw IllegalStateException()
        itemView.tag = itemView.tag ?: ItemViewHolderLegacy(itemView)

        val viewHolder = itemView.tag as ItemViewHolderLegacy
        viewHolder.topText.text = String.format(topTextPlaceholder, gameResult.score.toString())
        viewHolder.bottomText.text = gameResult.date.toString()

        return itemView
    }
}

/**
 * Screen used to display the game scores' history using a List View.
 */
class HistoryActivityLegacy : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_legacy)

        val history = findViewById<ListView>(R.id.historyList)
        val viewModel: HistoryViewModelLegacy by viewModels()

        viewModel.results.observe(this) {
            history.adapter = HistoryAdapterLegacy(this, it)
        }
    }
}