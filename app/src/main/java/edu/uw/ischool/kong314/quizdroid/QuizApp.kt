package edu.uw.ischool.kong314.quizdroid

import android.util.Log

class QuizApp : android.app.Application() {
    lateinit var topicRepository: TopicRepository
    override fun onCreate() {
        super.onCreate()
        Log.d("FromQuizApp", "Application started")
        topicRepository = TempTopicRepository()
    }
}