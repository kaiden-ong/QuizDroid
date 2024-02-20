package edu.uw.ischool.kong314.quizdroid

import android.util.Log
import androidx.preference.PreferenceManager

class QuizApp: android.app.Application() {
    lateinit var topicRepository: TopicRepository
    override fun onCreate() {
        super.onCreate()
        Log.d("FromQuizApp", "Application started")
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val quizUrl = sharedPreferences.getString("quiz_url", "")
        topicRepository = TempTopicRepository(this, quizUrl ?: "http://tednewardsandbox.site44.com/questions.json")
    }
}