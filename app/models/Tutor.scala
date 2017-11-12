package models

case class Tutor(
  firstName: String,
  lastName: String,
  description: String,
  avatarUrl: String,
  price: Double,
  subject: String,
  interest: String,
  matchingScore: Int
)