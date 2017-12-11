package models

import java.util.{ Date, UUID }

import models.enums.LessonType.LessonType

case class TutorProfile(
  userID: UUID,
  gender: String,
  dob: Date,
  mainLanguage: String,
  description: String,
  instituteAttended: String,
  occupation: String,
  occupationDegree: String,
  workingLanguage: String,
  subjectAbleToTeach: String,
  interest1: String,
  timeInterest1: Int,
  interest2: String,
  timeInterest2: Int,
  interest3: String,
  timeInterest3: Int,
  wishedSalary: Int,
  lessonType: LessonType
)
