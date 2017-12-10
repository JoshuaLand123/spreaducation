package models

import java.util.{ Date, UUID }

import models.enums.LessonType.LessonType

case class TutorProfile(
  userID: UUID,
  gender: String,
  dob: Date,
  description: String,
  classLevel: Int,
  institutesAttended: List[String],
  occupation: String,
  occupationDegree: String,
  mainLanguage: String,
  workingLanguages: List[String],
  subjectsAbleToTeach: List[String],
  interest1: String,
  timeInterest1: Int,
  interest2: String,
  timeInterest2: Int,
  interest3: String,
  timeInterest3: Int,
  uploadedDocuments: List[String],
  lessonType: LessonType,
  wishedSalary: Int
)

case class SubjectWithLevel(subject: String, level: Int)
