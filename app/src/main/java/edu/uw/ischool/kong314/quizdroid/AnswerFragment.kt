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


class AnswerFragment(private val topics: List<Topic>) : Fragment(R.layout.fragment_answer) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString("title")
        var question = arguments?.getString("question")
        var answers = arguments?.getStringArrayList("answers")
        var correctAnswer = arguments?.getInt("correctAnswer")
        val qNum = arguments?.getInt("qNum")
        val numCorrect = arguments?.getInt("numCorrect")
        val numQuestions = arguments?.getInt("totalQuestions")

        val correctAnswerText = answers!![correctAnswer!!]

        val questionNumber = view.findViewById<TextView>(R.id.qNumAns)
        val correct = view.findViewById<TextView>(R.id.correctAnswer)
        val ratioCorrect = view.findViewById<TextView>(R.id.numCorrect)

        questionNumber.text = "$title Question #$qNum"
        correct.text = "The correct answer was \"$correctAnswerText\"."
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
                Log.d("LateAnswer", "getting topic")
                val topic = topics.find { it.title == title }
                Log.d("LateAnswer", "getting question $qNum $question")
                question = topic!!.questions[qNum!!].questionText
                Log.d("LateAnswer", "getting answers")
                answers = ArrayList(topic.questions[qNum].answers)
                Log.d("LateAnswer", "getting correct")
                correctAnswer = topic.questions[qNum].correctAnswerIndex

                val questionFragment = QuestionFragment(topics)
                val args = Bundle().apply {
                    putString("title", title)
                    putString("question", question)
                    putStringArrayList("answers", answers)
                    putInt("correctAnswer", correctAnswer!! - 1)
                    putInt("qNum", qNum + 1)
                    putInt("numCorrect", numCorrect!!)
                    putInt("totalQuestions", numQuestions!!)
                }
                questionFragment.arguments = args
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainer, questionFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
    }
}