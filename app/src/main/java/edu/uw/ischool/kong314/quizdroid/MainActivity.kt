package edu.uw.ischool.kong314.quizdroid

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
            val longDescription = topic.longDescription
            val button = Button(this)
            button.text = title
            button.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            button.setBackgroundColor(Color.parseColor("#FF673AB7"))
            button.setTextColor(Color.WHITE)
            button.textSize = 24f
            button.setPadding(16, 16, 16, 16)
            container.addView(button)
            val desc = TextView(this)
            desc.text = shortDescription
            desc.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            desc.setTextColor(Color.parseColor("#FF000000"))
            desc.textSize = 16f // Set description text size
            desc.gravity = Gravity.CENTER_HORIZONTAL
            desc.setPadding(16, 8, 16, 75)
            container.addView(desc)
            button.setOnClickListener() {
                val fragment = OverviewFragment(topics)
                val args = Bundle().apply {
                    putString("title", title)
                    putString("longDesc", longDescription)
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