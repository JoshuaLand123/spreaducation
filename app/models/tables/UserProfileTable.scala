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
  def subjectImprove1 = column[String]("subject_improve_1")
  def scoreSubjectImprove1 = column[Int]("score_subject_improve_1")
  def interest1 = column[String]("interest_1")
  def timeInterest1 = column[String]("time_interest_1")
  def subjectGoodAt1 = column[String]("subject_goodat_1")
  def scoreSubjectGoodAt1 = column[Int]("score_subject_goodat_1")

  override def * = (userID, gender, dob, classLevel, schoolName, mainLanguage, subjectImprove1, scoreSubjectImprove1, interest1, timeInterest1, subjectGoodAt1, scoreSubjectGoodAt1) <> (UserProfile.tupled, UserProfile.unapply)

}
