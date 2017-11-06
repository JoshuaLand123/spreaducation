package models

import java.util.{ Date, UUID }

case class UserProfile(
  userID: UUID,
  gender: String,
  dob: Date,
  classLevel: Int,
  schoolName: String,
  mainLanguage: String,
  subjectImprove: String,
  scoreSubjectImprove: Int,
  subjectGoodAt: String,
  scoreSubjectGoodAt: Int,
  interest1: String,
  timeInterest1: Int,
  interest2: String,
  timeInterest2: Int,
  interest3: String,
  timeInterest3: Int
)
