package edu.uw.ischool.kong314.quizdroid

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TempTopicRepository(private val context: Context, private val urlString: String) : TopicRepository {
    override suspend fun getTopics(): List<Topic> {
        return withContext(Dispatchers.IO) {
            Log.d("fromRepo", "connecting")
            val topics = mutableListOf<Topic>()
            try {
//                Log.d("fromRepo", "trying connect URL:$urlString")
//                val connection = URL(urlString).openConnection() as HttpURLConnection
//                Log.d("fromRepo", "made connection")
//                val inputStream = connection.inputStream
//                Log.d("fromRepo", "made inputstream")
                val inputStream = context.assets.open("data.json")
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val jsonString = bufferedReader.use { it.readText() }
                val jsonArray = JSONArray(jsonString)
                Log.d("fromRepo", "to json")
                for (i in 0 until jsonArray.length()) {
                    val jsonTopic = jsonArray.getJSONObject(i)
                    val title = jsonTopic.getString("title")
                    val shortDescription = jsonTopic.getString("desc")
                    val longDescription = jsonTopic.getString("desc")

                    val questionsArray = jsonTopic.getJSONArray("questions")
                    val questions = mutableListOf<Question>()
                    for (j in 0 until questionsArray.length()) {
                        val questionObject = questionsArray.getJSONObject(j)
                        val questionText = questionObject.getString("text")
                        val answersArray = questionObject.getJSONArray("answers")
                        val answers = mutableListOf<String>()
                        for (k in 0 until answersArray.length()) {
                            answers.add(answersArray.getString(k))
                        }
                        val correctAnswerIndex = questionObject.getInt("answer")
                        questions.add(Question(questionText, answers, correctAnswerIndex))
                    }
                    topics.add(Topic(title, shortDescription, longDescription, questions))
                }
                inputStream.close()
            } catch (e: Exception) {
                Log.d("fromRepo", "couldnt connect URL:$urlString")
                e.printStackTrace()
            }
            for (topic in topics) {
                Log.d("ParsedTopic", topic.toString())
            }
            topics
        }
    }
}