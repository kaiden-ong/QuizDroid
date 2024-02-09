package edu.uw.ischool.kong314.quizdroid

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class OverviewFragment : Fragment(R.layout.fragment_overview) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title")
        val description = arguments?.getString("description")
        val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        val descriptionText = view.findViewById<TextView>(R.id.description)
        titleTextView.text = "$title Test Overview"
        descriptionText.text = description

        val beginBtn = view.findViewById<Button>(R.id.begin)
        beginBtn.setOnClickListener() {
            val jsonObject = JSONObject(loadJsonFromAsset(requireContext(), "data.json"))
            val topicsArray = jsonObject.getJSONArray("topics")
            var question: String? = null
            var answers: ArrayList<String>? = null
            var numQuestions = 0
            var correctAnswer: String? = null
            for (i in 0 until topicsArray.length()) {
                val topicObject = topicsArray.getJSONObject(i)
                if (topicObject.getString("title") == title) {
                    val questionsArray = topicObject.getJSONArray("questions")
                    numQuestions = questionsArray.length()
                    val firstQuestionObject = questionsArray.getJSONObject(0)
                    question = firstQuestionObject.getString("question")
                    answers = ArrayList()
                    for (j in 0 until firstQuestionObject.getJSONArray("answers").length()) {
                        answers.add(firstQuestionObject.getJSONArray("answers").getString(j))
                    }
                    correctAnswer = firstQuestionObject.getString("correctAnswer")
                    break
                }
            }
            val questionFragment = QuestionFragment()
            Log.d("fromoverview", numQuestions.toString())
            val args = Bundle().apply {
                putString("title", title)
                putString("question", question)
                putStringArrayList("answers", answers)
                putString("correctAnswer", correctAnswer)
                putInt("qNum", 1)
                putInt("numCorrect", 0)
                putInt("totalQuestions", numQuestions)
            }
            Log.d("CHECKINGBACKSTACK", requireActivity().supportFragmentManager.backStackEntryCount.toString())
            questionFragment.arguments = args
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, questionFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
    private fun loadJsonFromAsset(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use {
            it.readText()
        }
    }
}
