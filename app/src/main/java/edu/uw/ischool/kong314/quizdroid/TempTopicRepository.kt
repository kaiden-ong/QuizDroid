package edu.uw.ischool.kong314.quizdroid

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class TempTopicRepository(private val context: Context, private val urlString: String) : TopicRepository {
    private var isDownloading: Boolean = false

    init {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (sharedPreferences.contains("duration")) {
            startDownloadService()
        }
    }
    override suspend fun getTopics(): List<Topic> {

        return withContext(Dispatchers.IO) {
            val localFile = File(context.filesDir, "questions.json")
            return@withContext downloadTopics(urlString, localFile, context)
        }
    }

    private suspend fun downloadTopics(urlString: String, localFile: File, context: Context): List<Topic> {
        isDownloading = true
        var newJsonString: String? = null
        var success = false
        withContext(Dispatchers.IO) {
            try {
                val connection = URL(urlString).openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val contentType = connection.contentType
                if (contentType == "application/json") {
                    newJsonString = inputStream.bufferedReader().use { it.readText() }
                    success = true
                } else {
                    Log.e("TempTopicRepository", "Not a json file")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        return if (success && newJsonString != null) {
            editor.putBoolean("success", true)
            editor.apply()
            saveFile(newJsonString!!, localFile)
            isDownloading = false
            parseFromJson(newJsonString!!)
        } else {
            editor.putBoolean("success", false)
            editor.apply()
            isDownloading = false
            if (localFile.exists()) {
                readFile(localFile)
            } else {
                emptyList()
            }
        }
    }

    private fun parseFromJson(jsonString: String): List<Topic> {
        val topics = mutableListOf<Topic>()
        val jsonArray = JSONArray(jsonString)
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

    private fun startDownloadService() {
        if (!isDownloading) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val frequency = sharedPreferences.getString("duration", "")?.toLongOrNull() ?: return
            val executor = Executors.newSingleThreadScheduledExecutor()
            executor.scheduleAtFixedRate({
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d("Downloadcheck", "downloading")
                    getTopics()
                }
            }, 0, frequency, TimeUnit.MINUTES)
        }
    }
}