package edu.uw.ischool.kong314.quizdroid

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), PreferencesFragment.OnPreferencesChangedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        File(filesDir, "questions.json").delete()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.remove("duration")
        editor.putString("quiz_url", "http://tednewardsandbox.site44.com/questions.json")
        editor.apply()
        Toast.makeText(this, sharedPreferences.getString("quiz_url", ""), Toast.LENGTH_SHORT).show()

        val quizApp = application as QuizApp
        val container = findViewById<ViewGroup>(R.id.buttonContainer)
        CoroutineScope(Dispatchers.Main).launch {
            val topics = quizApp.topicRepository.getTopics()
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

    override fun onResume() {
        super.onResume()
        val quizApp = application as QuizApp
        val container = findViewById<ViewGroup>(R.id.buttonContainer)
        CoroutineScope(Dispatchers.Main).launch {
            val topics = quizApp.topicRepository.getTopics()
            makeButtons(topics)
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
        if (isAirplaneModeOn(this)) {
            askToDisableAirplaneMode(this)
        } else {
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
                button.setOnClickListener {
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
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            if (!sharedPreferences.getBoolean("success", true)) {
                AlertDialog.Builder(this)
                    .setTitle("Invalid JSON URL")
                    .setMessage("The URL you entered is not a valid JSON file")
                    .setPositiveButton("Retry") { dialog, _ ->
                        dialog.cancel()
                    }
                    .setNegativeButton("Quit") { _, _ ->
                        exitProcess(0)
                    }
                    .setCancelable(false) // Prevent dialog from being dismissed by tapping outside or pressing back button
                    .show()
            }
        }
    }

    private fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
    }

    private fun askToDisableAirplaneMode(context: Context) {
        AlertDialog.Builder(this)
            .setTitle("Airplane Mode")
            .setMessage("Your device is in airplane mode. Do you want to turn it off?")
            .setPositiveButton("Yes") { _, _ ->
                val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
                context.startActivity(intent)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}