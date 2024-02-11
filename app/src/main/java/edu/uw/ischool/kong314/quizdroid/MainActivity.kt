package edu.uw.ischool.kong314.quizdroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val quizApp = application as QuizApp
        val topics = quizApp.topicRepository.getTopics()
        val container = findViewById<ViewGroup>(R.id.buttonContainer)
        Log.d("FromMain", "Making btn")
        topics.forEach { topic ->
            val title = topic.title
            val shortDescription = topic.shortDescription
            val button = Button(this)
            button.text = title
            button.height = 100
            button.setBackgroundColor(0xFFbe03fc.toInt())
            container.addView(button)
            button.setOnClickListener() {
                val fragment = OverviewFragment()
                val args = Bundle().apply {
                    putString("title", title)
                    putString("description", shortDescription)
                }
                fragment.arguments = args
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        supportFragmentManager.addOnBackStackChangedListener {
            container.visibility = if (supportFragmentManager.backStackEntryCount == 0) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
    }
}