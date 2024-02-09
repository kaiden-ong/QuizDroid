package edu.uw.ischool.kong314.quizdroid

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class QuestionFragment : Fragment(R.layout.fragment_question) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title")
        val question = arguments?.getString("question")
        val answers = arguments?.getStringArrayList("answers")
        val correctAnswer = arguments?.getString("correctAnswer")
        val qNum = arguments?.getInt("qNum")
        var numCorrect = arguments?.getInt("numCorrect")
        val numQuestions = arguments?.getInt("totalQuestions")
        Log.d("fromquestions", numQuestions.toString())

        val questionNumber = view.findViewById<TextView>(R.id.qNum)
        val questionDesc = view.findViewById<TextView>(R.id.question)
        questionNumber.text = "$title Question #$qNum"
        questionDesc.text = question
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        if (answers != null) {
            for (i in answers.indices) {
                Log.d("fromquestionfragment", "try to insert")
                val radioButton = RadioButton(requireContext())
                radioButton.text = answers[i]
                radioButton.textSize = 24F
                radioButton.id = i
                radioGroup.addView(radioButton)
            }
        }

        val nextBtn = view.findViewById<Button>(R.id.button)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                nextBtn.visibility = View.VISIBLE
            } else {
                nextBtn.visibility = View.INVISIBLE
            }
        }

        nextBtn.setOnClickListener() {
            val questionFragment = AnswerFragment()
            val selectedRadioButton = view.findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
            if (correctAnswer == selectedRadioButton.text.toString()) {
                numCorrect = numCorrect!! + 1
            }
            val args = Bundle().apply {
                putString("title", title)
                putString("question", question)
                putString("correctAnswer", correctAnswer)
                putInt("qNum", qNum!!)
                putInt("numCorrect", numCorrect!!)
                putInt("totalQuestions", numQuestions!!)
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