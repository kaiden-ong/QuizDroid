package edu.uw.ischool.kong314.quizdroid

interface TopicRepository {
    fun getTopics(): List<Topic>
}