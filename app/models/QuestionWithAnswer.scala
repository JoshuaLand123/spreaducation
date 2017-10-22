package models

case class QuestionWithAnswer(
  questionId: Int,
  answerId: Option[Int],
  questionText: String,
  score: Int
)