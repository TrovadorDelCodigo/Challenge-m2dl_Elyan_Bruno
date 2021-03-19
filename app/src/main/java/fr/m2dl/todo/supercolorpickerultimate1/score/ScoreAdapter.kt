package fr.m2dl.todo.supercolorpickerultimate1.score

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import fr.m2dl.todo.supercolorpickerultimate1.R

class ScoreAdapter(
    context: Context,
    layoutId: Int,
    scores: List<Score>
): ArrayAdapter<Score>(context, layoutId, scores) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val score = getItem(position)!!

        val view = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.score_item, parent, false)

        val playerNameTextView = view.findViewById<TextView>(R.id.score_item_player_name)
        val scoreTextView = view.findViewById<TextView>(R.id.score_item_score)

        playerNameTextView.text = score.playerName
        scoreTextView.text = "${score.score}"

        return view
    }
}
