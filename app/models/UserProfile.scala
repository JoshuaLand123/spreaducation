package models

import java.util.{ Date, UUID }

case class UserProfile(
  userID: UUID,
  gender: String,
  dob: Date,
  classLevel: Int,
  schoolName: String,
  mainLanguage: String,
  subjectImprove1: String,
  scoreSubjectImprove1: Int,
  interest1: String,
  timeInterest1: String,
  subjectGoodAt1: String,
  scoreSubjectGoodAt1: Int
)
