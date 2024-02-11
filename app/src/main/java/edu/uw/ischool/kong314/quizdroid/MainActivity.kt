package edu.uw.ischool.kong314.quizdroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.util.Log
import android.view.View
import android.view.ViewGroup
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

//        val jsonString = loadJsonFromAsset("data.json")
//        val topics = parseJson(jsonString)

        val container = findViewById<ViewGroup>(R.id.buttonContainer)
        Log.d("FromMain", "Making btn")
        topics.forEach { topic ->
            val title = topic.title
            val description = topic.description
            val button = Button(this)
            button.text = title
            button.height = 100
            button.setBackgroundColor(0xFFbe03fc.toInt())
            container.addView(button)
            button.setOnClickListener() {
                val fragment = OverviewFragment()
                val args = Bundle().apply {
                    putString("title", title)
                    putString("description", description)
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

//    private fun loadJsonFromAsset(fileName: String): String {
//        val reader = BufferedReader(InputStreamReader(assets.open(fileName)))
//        return reader.use { it.readText() }
//    }
//
//    private fun parseJson(jsonString: String): List<Topic> {
//        val topics = mutableListOf<Topic>()
//        val jsonObject = JSONObject(jsonString)
//        val topicsArray = jsonObject.getJSONArray("topics")
//        for (i in 0 until topicsArray.length()) {
//            val topicObject = topicsArray.getJSONObject(i)
//            val title = topicObject.getString("title")
//            val description = topicObject.getString("description")
//            topics.add(Topic(title, description))
//        }
//        return topics
//    }
}