package models

import java.util.{ Date, UUID }

case class TuteeProfile(
  userID: UUID,
  gender: String,
  dob: Date,
  classLevel: Int,
  schoolName: String,
  learningLanguage: String,
  description: String,
  subjects: TuteeSubjects,
  interests: TuteeInterests,
  lessonType: String,
  skype: Option[String],
  address: Option[Address]
)

case class TuteeSubjects(
  subjectImprove1: String,
  scoreSubjectImprove1: Int,
  subjectImprove2: String,
  scoreSubjectImprove2: Int,
  subjectGoodAt1: String,
  scoreSubjectGoodAt1: Int,
  subjectGoodAt2: String,
  scoreSubjectGoodAt2: Int
)
case class TuteeInterests(
  interest1: String,
  timeInterest1: Int,
  interest2: String,
  timeInterest2: Int,
  interest3: String,
  timeInterest3: Int
)

case class Address(
  street: String,
  postalCode: String
)