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
  subjectImprove2: String,
  scoreSubjectImprove2: Int,
  subjectGoodAt1: String,
  scoreSubjectGoodAt1: Int,
  subjectGoodAt2: String,
  scoreSubjectGoodAt2: Int,
  interest1: String,
  timeInterest1: Int,
  interest2: String,
  timeInterest2: Int,
  interest3: String,
  timeInterest3: Int,
  tutorOrder: Option[Int] = None
)
