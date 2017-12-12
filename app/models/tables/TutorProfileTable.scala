package models
package tables

import java.util.{ Date, UUID }

import models.enums.LessonType.LessonType
import slick.jdbc.PostgresProfile.api._

class TutorProfileTable(tag: Tag) extends Table[TutorProfile](tag, "tutor_profile") {

  def userID = column[UUID]("user_id", O.PrimaryKey)
  def gender = column[String]("gender")
  def dob = column[Date]("dob")
  def mainLanguage = column[String]("main_language")
  def description = column[String]("description")
  def instituteAttended = column[String]("institute_attended")
  def occupation = column[String]("occupation")
  def occupationDegree = column[String]("occupation")
  def workingLanguage = column[String]("working_language")
  def subjectsAbleToTeach = column[String]("subject_able_to_teach")
  def interest1 = column[String]("interest_1")
  def timeInterest1 = column[Int]("time_interest_1")
  def interest2 = column[String]("interest_2")
  def timeInterest2 = column[Int]("time_interest_2")
  def interest3 = column[String]("interest_3")
  def timeInterest3 = column[Int]("time_interest_3")
  def wishedSalary = column[Double]("wished_salary")
  def lessonType = column[String]("lesson_type")

  override def * = (userID, gender, dob, mainLanguage, description, instituteAttended, occupation, occupationDegree, workingLanguage, subjectsAbleToTeach, interest1, timeInterest1, interest2, timeInterest2, interest3, timeInterest3, wishedSalary, lessonType) <> (TutorProfile.tupled, TutorProfile.unapply)

}
