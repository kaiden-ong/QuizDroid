package edu.uw.ischool.kong314.quizdroid

import android.app.Application
import android.util.Log

class QuizApp : Application() {
    val topicRepository: TopicRepository by lazy { TempTopicRepository() }
    override fun onCreate() {
        super.onCreate()
        Log.d("QuizApp", "Application started")
    }
}