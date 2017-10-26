package models

case class QuestionModel(
  questionWithAnswers: Seq[(Question, Option[Answer])],
  total: Int
)