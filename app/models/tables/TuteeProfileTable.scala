package models
package tables

import java.util.{ Date, UUID }

import slick.jdbc.PostgresProfile.api._

class TuteeProfileTable(tag: Tag) extends Table[TuteeProfile](tag, "tutee_profile") {

  def userID = column[UUID]("user_id", O.PrimaryKey)
  def gender = column[String]("gender")
  def dob = column[Date]("dob")
  def classLevel = column[Int]("class_level")
  def schoolName = column[String]("school_name")
  def mainLanguage = column[String]("main_language")
  def subjectImprove1 = column[String]("subject_improve_1")
  def scoreSubjectImprove1 = column[Int]("score_subject_improve_1")
  def subjectImprove2 = column[String]("subject_improve_2")
  def scoreSubjectImprove2 = column[Int]("score_subject_improve_2")
  def subjectGoodAt1 = column[String]("subject_goodat_1")
  def scoreSubjectGoodAt1 = column[Int]("score_subject_goodat_1")
  def subjectGoodAt2 = column[String]("subject_goodat_2")
  def scoreSubjectGoodAt2 = column[Int]("score_subject_goodat_2")
  def interest1 = column[String]("interest_1")
  def timeInterest1 = column[Int]("time_interest_1")
  def interest2 = column[String]("interest_2")
  def timeInterest2 = column[Int]("time_interest_2")
  def interest3 = column[String]("interest_3")
  def timeInterest3 = column[Int]("time_interest_3")
  def tutorOrder = column[Option[Int]]("tutor_order")
  def tutorID = column[Option[UUID]]("tutor_id")

  override def * = (userID, gender, dob, classLevel, schoolName, mainLanguage, subjectImprove1, scoreSubjectImprove1, subjectImprove2, scoreSubjectImprove2, subjectGoodAt1, scoreSubjectGoodAt1, subjectGoodAt2, scoreSubjectGoodAt2, interest1, timeInterest1, interest2, timeInterest2, interest3, timeInterest3, tutorOrder, tutorID) <> (TuteeProfile.tupled, TuteeProfile.unapply)

}
