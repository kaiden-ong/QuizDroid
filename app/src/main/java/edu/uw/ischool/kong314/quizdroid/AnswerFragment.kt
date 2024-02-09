package edu.uw.ischool.kong314.quizdroid

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.json.JSONObject

class AnswerFragment : Fragment(R.layout.fragment_answer) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString("title")
        val question = arguments?.getString("question")
        val correctAnswer = arguments?.getString("correctAnswer")
        val qNum = arguments?.getInt("qNum")
        var numCorrect = arguments?.getInt("numCorrect")

        val questionNumber = view.findViewById<TextView>(R.id.qNumAns)
        val correct = view.findViewById<TextView>(R.id.correctAnswer)
        val ratioCorrect = view.findViewById<TextView>(R.id.numCorrect)

        questionNumber.text = "$title Question #$qNum"
        correct.text = "The correct answer was \"$correctAnswer\"."
        ratioCorrect.text = "You have gotten $numCorrect out of $qNum correct."

        val button = view.findViewById<Button>(R.id.nextQuestion)
        button.setOnClickListener() {
            val jsonObject = JSONObject(loadJsonFromAsset(requireContext(), "data.json"))
            val topicsArray = jsonObject.getJSONArray("topics")
            var question: String? = null
            var answers: ArrayList<String>? = null
            var correctAnswer: String? = null
            for (i in 0 until topicsArray.length()) {
                val topicObject = topicsArray.getJSONObject(i)
                if (topicObject.getString("title") == title) {
                    val questionsArray = topicObject.getJSONArray("questions")
                    val firstQuestionObject = questionsArray.getJSONObject(qNum!!)
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
            val args = Bundle().apply {
                putString("title", title)
                putString("question", question)
                putStringArrayList("answers", answers)
                putString("correctAnswer", correctAnswer)
                putInt("qNum", qNum!! + 1)
                putInt("numCorrect", numCorrect!!)
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

private fun loadJsonFromAsset(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use {
        it.readText()
    }
}