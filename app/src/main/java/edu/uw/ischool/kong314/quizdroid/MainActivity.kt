package edu.uw.ischool.kong314.quizdroid

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class MainActivity : AppCompatActivity(), PreferencesFragment.OnPreferencesChangedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPreferences.edit()
        editor.clear()
//        editor.putString("quiz_url", "http://tednewardsandbox.site44.com/questions.json")
        editor.apply()

        File(filesDir, "questions.json").delete()

        val quizApp = application as QuizApp
        val container = findViewById<ViewGroup>(R.id.buttonContainer)
        CoroutineScope(Dispatchers.Main).launch {
            val topics = quizApp.topicRepository.getTopics()
            Log.d("FromMain", "Making btn")
            makeButtons(topics)
        }

        supportFragmentManager.addOnBackStackChangedListener {
            container.visibility = if (supportFragmentManager.backStackEntryCount == 0) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.preferences, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.preferences -> {
                val fragment = PreferencesFragment()
                fragment.listener = this
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPreferencesChanged() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val url = sharedPreferences.getString("quiz_url", "")
        Log.d("FromMAIN", "PREF CHANGE, URL:$url")
        (application as QuizApp).topicRepository = TempTopicRepository(this, url ?: "")
        CoroutineScope(Dispatchers.Main).launch {
            val topics = (application as QuizApp).topicRepository.getTopics()
            makeButtons(topics)
        }
    }

    private fun makeButtons(topics: List<Topic>) {
        val container = findViewById<ViewGroup>(R.id.buttonContainer)
        container.removeAllViews()
        for (topic in topics) {
            Log.d("MainParsedTopic", topic.toString())
            val title = topic.title
            val shortDescription = topic.shortDescription
            val longDescription = topic.longDescription
            val button = Button(this@MainActivity)
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
            val desc = TextView(this@MainActivity)
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
    }
}