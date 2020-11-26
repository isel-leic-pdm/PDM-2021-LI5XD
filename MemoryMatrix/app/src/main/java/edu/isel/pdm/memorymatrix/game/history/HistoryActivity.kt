package edu.isel.pdm.memorymatrix.game.history

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.isel.pdm.memorymatrix.MemoryMatrixApplication
import edu.isel.pdm.memorymatrix.R
import edu.isel.pdm.memorymatrix.utils.BaseActivity

/**
 * The recyclerview´s view holder
 */
class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val topText: TextView = itemView.findViewById(R.id.historyItemTopText)
    val bottomText: TextView = itemView.findViewById(R.id.historyItemBottomText)
}

/**
 * The recyclerview´s adapter
 */
class HistoryAdapter(
        private val ctx: Context,
        private val history: List<GameResult>
    ) : RecyclerView.Adapter<ItemViewHolder>() {

    private val topFormatString = ctx.resources.getString(R.string.history_item_score)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.v((ctx.applicationContext as MemoryMatrixApplication).appTag,
            "onCreateViewHolder")
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.v((ctx.applicationContext as MemoryMatrixApplication).appTag,
            "position = $position of $itemCount")
        holder.topText.text = String.format(topFormatString, history[position].score.toString())
        holder.bottomText.text = history[position].date.toString()
    }

    override fun getItemCount() = history.size
}

/**
 * Screen used to display the game scores' history using a Recycler View.
 */
class HistoryActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val history = findViewById<RecyclerView>(R.id.historyView)
        history.setHasFixedSize(true)
        history.layoutManager = LinearLayoutManager(this)

        val viewModel: HistoryViewModel by viewModels()

        viewModel.results.observe(this) {
            history.adapter = HistoryAdapter(this, it)
        }
    }
}