package edu.uw.ischool.kong314.quizdroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.json.JSONObject


class AnswerFragment : Fragment(R.layout.fragment_answer) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString("title")
        val question = arguments?.getString("question")
        val correctAnswer = arguments?.getString("correctAnswer")
        val qNum = arguments?.getInt("qNum")
        var numCorrect = arguments?.getInt("numCorrect")
        val numQuestions = arguments?.getInt("totalQuestions")

        val questionNumber = view.findViewById<TextView>(R.id.qNumAns)
        val correct = view.findViewById<TextView>(R.id.correctAnswer)
        val ratioCorrect = view.findViewById<TextView>(R.id.numCorrect)

        questionNumber.text = "$title Question #$qNum"
        correct.text = "The correct answer was \"$correctAnswer\"."
        ratioCorrect.text = "You have gotten $numCorrect out of $qNum correct."
        Log.d("CHECKINGPARAMS", "num correct: $numCorrect, num questions: $numQuestions, qNum: $qNum")
        val button = view.findViewById<Button>(R.id.nextQuestion)
        Log.d("FromAnswers", numQuestions.toString() + " " + qNum.toString())
        if (numQuestions == qNum) {
            button.text = "Finish"
            button.setOnClickListener() {
                val fm: FragmentManager = requireActivity().supportFragmentManager
                for (i in 0 until fm.backStackEntryCount) {
                    fm.popBackStack()
                }
            }
        } else {
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
                    putInt("totalQuestions", numQuestions!!)
                }
                questionFragment.arguments = args
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainer, questionFragment)
                transaction.commit()
            }
        }
    }
}

private fun loadJsonFromAsset(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use {
        it.readText()
    }
}