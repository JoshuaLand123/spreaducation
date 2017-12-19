package models

import java.util.UUID

case class TutorMatch(
  userID: UUID,
  firstName: String,
  lastName: String,
  description: String,
  avatarUrl: Option[String],
  price: Double,
  subject1: String,
  subject2: Option[String] = None,
  interest: String,
  matchingScore: Int,
  status: String,
  order: Int,
  isFake: Boolean
)

case class TutorMatchDB(
  userID: UUID,
  firstName: String,
  lastName: String,
  description: String,
  avatarUrl: Option[String],
  price: Double,
  subject1: String,
  subject1Level: Int,
  subject2: Option[String] = None,
  subject2Level: Option[Int] = None,
  subject3: Option[String] = None,
  subject3Level: Option[Int] = None,
  subject4: Option[String] = None,
  subject4Level: Option[Int] = None,
  interest1: String,
  interest2: Option[String] = None,
  interest3: Option[String] = None
)
