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

class OverviewFragment(private val topics: List<Topic>) : Fragment(R.layout.fragment_overview) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title")
        val longDesc = arguments?.getString("longDesc")
        val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        val descriptionText = view.findViewById<TextView>(R.id.description)
        titleTextView.text = "$title Test Overview"
        descriptionText.text = longDesc

        val beginBtn = view.findViewById<Button>(R.id.begin)
        beginBtn.setOnClickListener() {
            val topic = topics.find { it.title == title }
            val qNum = 0
            val question: String = topic!!.questions[qNum].questionText
            val answers: ArrayList<String> = ArrayList(topic.questions[qNum].answers)
            val numQuestions = topic.questions.size
            val correctAnswer: Int = topic.questions[qNum].correctAnswerIndex - 1
            val questionFragment = QuestionFragment(topics)
            Log.d("fromoverview", numQuestions.toString())
            val args = Bundle().apply {
                putString("title", title)
                putString("question", question)
                putStringArrayList("answers", answers)
                putInt("correctAnswer", correctAnswer)
                putInt("qNum", qNum + 1)
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
}