package models

import java.util.UUID

case class Answer(
  id: Option[Int] = None,
  userId: UUID,
  questionId: Int,
  score: Int
)