package fr.m2dl.todo.supercolorpickerultimate1

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import fr.m2dl.todo.supercolorpickerultimate1.dialogs.PlayerNameDialog
import fr.m2dl.todo.supercolorpickerultimate1.score.Score
import fr.m2dl.todo.supercolorpickerultimate1.score.ScoreAdapter
import fr.m2dl.todo.supercolorpickerultimate1.score.db.ScoreDB
import fr.m2dl.todo.supercolorpickerultimate1.score.db.impl.FirebaseScoreDB

class GameOverActivity : FragmentActivity() {

    private lateinit var scoreDB: ScoreDB
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindow()
        setContentView(R.layout.activity_game_over)

        scoreDB = FirebaseScoreDB()
        score = intent.getIntExtra("score", 0)
        displayScore()
        displayHighScores()
    }

    private fun setupWindow() {
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    private fun displayScore() {
        val scoreTextView = findViewById<TextView>(R.id.scoreText)
        val scoreText = getString(R.string.score)
        scoreTextView.text = "$scoreText $score"
    }

    private fun displayHighScores() {
        scoreDB.getScores { scores ->
            val sortedScores = scores.sortedByDescending { it.score }
            val scoreAdapter = ScoreAdapter(this, R.layout.score_item, sortedScores)
            val scoreListView = findViewById<ListView>(R.id.scoresListView)
            scoreListView.adapter = scoreAdapter
        }
    }

    fun registerScore(view: View) {
        val playerNameDialog = PlayerNameDialog()
        playerNameDialog.onPlayerNameEntered = { playerName ->
            scoreDB.registerScore(Score(playerName, score))
            findViewById<Button>(R.id.register_score_button).visibility = View.GONE
            displayHighScores() // Refresh the scores
        }

        playerNameDialog.show(supportFragmentManager, "player_name")
    }
}
