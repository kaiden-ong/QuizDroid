package edu.uw.ischool.kong314.quizdroid

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import java.io.BufferedReader
import java.io.InputStreamReader
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class TempTopicRepository(private val context: Context, private val urlString: String) : TopicRepository {
    override suspend fun getTopics(): List<Topic> {
        Log.d("fromrepo", "getting topics")
        return withContext(Dispatchers.IO) {
            val localFile = File(context.filesDir, "questions.json")
            return@withContext downloadTopics(urlString, localFile)
        }
    }

    private suspend fun downloadTopics(urlString: String, localFile: File): List<Topic> {
        var newJsonString: String? = null
        withContext(Dispatchers.IO) {
            try {
                val connection = URL(urlString).openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                newJsonString = inputStream.bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        Log.d("fromRepo", "downloading")
        if (newJsonString != null) {
            saveFile(newJsonString!!, localFile)
            return parseFromJson(newJsonString!!)
        }
        if (localFile.exists()) {
            return readFile(localFile)
        }
        return emptyList()
    }

    private fun parseFromJson(jsonString: String): List<Topic> {
        val topics = mutableListOf<Topic>()
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
        return topics
    }

    private fun saveFile(jsonString: String, localFile: File) {
        Log.d("fromrepo", "saving file")
        FileOutputStream(localFile).use { outputStream ->
            outputStream.write(jsonString.toByteArray())
        }
    }

    private fun readFile(file: File): List<Topic> {
        val stringBuilder = StringBuilder()
        FileInputStream(file).use { inputStream ->
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } != -1) {
                stringBuilder.append(String(buffer, 0, length))
            }
        }
        return parseFromJson(stringBuilder.toString())
    }
}