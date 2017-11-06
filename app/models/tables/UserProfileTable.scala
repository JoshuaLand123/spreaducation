package models
package tables

import java.util.{ Date, UUID }

import slick.jdbc.PostgresProfile.api._

class UserProfileTable(tag: Tag) extends Table[UserProfile](tag, "user_profile") {

  def userID = column[UUID]("user_id", O.PrimaryKey)
  def gender = column[String]("gender")
  def dob = column[Date]("dob")
  def classLevel = column[Int]("class_level")
  def schoolName = column[String]("school_name")
  def mainLanguage = column[String]("main_language")
  def subjectImprove = column[String]("subject_improve")
  def scoreSubjectImprove = column[Int]("score_subject_improve")
  def subjectGoodAt = column[String]("subject_goodat")
  def scoreSubjectGoodAt = column[Int]("score_subject_goodat")
  def interest1 = column[String]("interest_1")
  def timeInterest1 = column[Int]("time_interest_1")
  def interest2 = column[String]("interest_2")
  def timeInterest2 = column[Int]("time_interest_2")
  def interest3 = column[String]("interest_3")
  def timeInterest3 = column[Int]("time_interest_3")

  override def * = (userID, gender, dob, classLevel, schoolName, mainLanguage, subjectImprove, scoreSubjectImprove, subjectGoodAt, scoreSubjectGoodAt, interest1, timeInterest1, interest2, timeInterest2, interest3, timeInterest3) <> (UserProfile.tupled, UserProfile.unapply)

}
