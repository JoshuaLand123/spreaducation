package models

import models.DiscType.DiscType
import models.UserType.UserType

case class Question(
  id: Int,
  questionText: String,
  questionUserType: UserType,
  discCategory: DiscType,
  psychoCategory: Option[String],
  psychoSubCategory: Option[String],
  psychoScoreReverse: Option[Boolean],
  questionOrder: Int
)