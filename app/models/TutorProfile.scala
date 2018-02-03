package models

import java.util.{ Date, UUID }

case class TutorProfile(
  userID: UUID,
  gender: String,
  dob: Date,
  description: String,
  instituteAttended: String,
  occupation: String,
  occupationDegree: String,
  teachingLanguage: String,
  subjects: TutorSubjects,
  interests: TutorInterests,
  wishedSalary: Double,
  lessonType: String,
  postalCode: Option[String]
)

case class TutorSubjects(subject1: String, subject1Level: Int, subject2: Option[String], subject2Level: Option[Int], subject3: Option[String], subject3Level: Option[Int], subject4: Option[String], subject4Level: Option[Int])
case class TutorInterests(interest1: String, timeInterest1: Int, interest2: String, timeInterest2: Int, interest3: String, timeInterest3: Int)

