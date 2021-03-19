package fr.m2dl.todo.supercolorpickerultimate1.score.db.impl

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import fr.m2dl.todo.supercolorpickerultimate1.score.Score
import fr.m2dl.todo.supercolorpickerultimate1.score.db.ScoreDB
import java.util.UUID

class FirebaseScoreDB: ScoreDB {
    private val databaseUrl = "https://super-color-picker-ultimate-1-default-rtdb.firebaseio.com/"
    private var database: DatabaseReference = Firebase.database(databaseUrl).reference

    override fun registerScore(score: Score) {
        database.child("scores")
                .child(UUID.randomUUID().toString())
                .setValue(score)
    }

    override fun getScores(scoresHandler: (List<Score>) -> Unit) {
        database.child("scores")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val scores = mutableListOf<Score>()

                        snapshot.children.forEach {
                            val deserializedScore = it.getValue<DeserializedScore>()!!
                            scores += Score(deserializedScore.playerName!!, deserializedScore.score!!)
                        }
                        scoresHandler(scores)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
    }
}
