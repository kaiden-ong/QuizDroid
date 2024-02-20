package edu.uw.ischool.kong314.quizdroid

interface TopicRepository {
    suspend fun getTopics(): List<Topic>
}