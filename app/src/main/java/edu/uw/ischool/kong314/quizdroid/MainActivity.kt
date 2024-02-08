package edu.uw.ischool.kong314.quizdroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mathBtn = findViewById<Button>(R.id.mathBtn)
        val physicsButton = findViewById<Button>(R.id.physicsBtn)
        val marvelButton = findViewById<Button>(R.id.marvelBtn)

        val mathFragment = MathFragment()
        val physicsFragment = PhysicsFragment()
        val marvelFragment = MarvelFragment()
        Log.d("FromMain", "btns and fragments made")
        mathBtn.setOnClickListener() {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, mathFragment)
                commit()
            }
        }

        physicsButton.setOnClickListener() {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, physicsFragment)
                commit()
            }
        }

        marvelButton.setOnClickListener() {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, marvelFragment)
                commit()
            }
        }
    }
}