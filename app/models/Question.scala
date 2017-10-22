package models

import models.UserType.UserType

case class Question(
  id: Int,
  questionText: String,
  questionUserType: UserType,
  questionOrder: Int
)