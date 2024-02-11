package edu.uw.ischool.kong314.quizdroid


data class Question(
    val questionText: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)

data class Topic(
    val title: String,
    val shortDescription: String,
    val longDescription: String,
    val questions: List<Question>
)