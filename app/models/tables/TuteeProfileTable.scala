package models
package tables

import java.util.{ Date, UUID }

import slick.collection.heterogeneous.HNil
import slick.collection.heterogeneous.syntax.{ ::, HNil }
import slick.jdbc.PostgresProfile.api._

class TuteeProfileTable(tag: Tag) extends Table[TuteeProfile](tag, "tutee_profile") {

  def userID = column[UUID]("user_id", O.PrimaryKey)
  def gender = column[String]("gender")
  def dob = column[Date]("dob")
  def classLevel = column[Int]("class_level")
  def schoolName = column[String]("school_name")
  def learningLanguage = column[String]("learning_language")
  def description = column[String]("description")
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
  def lessonType = column[String]("lesson_type")
  def skype = column[Option[String]]("skype")
  def street = column[Option[String]]("street")
  def postalCode = column[Option[String]]("postal_code")

  override def * = (userID :: gender :: dob :: classLevel :: schoolName :: learningLanguage :: description :: subjectImprove1 :: scoreSubjectImprove1 :: subjectImprove2 :: scoreSubjectImprove2 :: subjectGoodAt1 :: scoreSubjectGoodAt1 :: subjectGoodAt2 :: scoreSubjectGoodAt2 :: interest1 :: timeInterest1 :: interest2 :: timeInterest2 :: interest3 :: timeInterest3 :: lessonType :: skype :: street :: postalCode :: HNil) <> (tuple, unapply)

  type TuteeProfileHList = UUID :: String :: Date :: Int :: String :: String :: String :: String :: Int :: String :: Int :: String :: Int :: String :: Int :: String :: Int :: String :: Int :: String :: Int :: String :: Option[String] :: Option[String] :: Option[String] :: HNil

  def tuple(data: TuteeProfileHList): TuteeProfile = data match {

    case userID :: gender :: dob :: classLevel :: schoolName :: learningLanguage :: description :: subjectImprove1 :: scoreSubjectImprove1 :: subjectImprove2 :: scoreSubjectImprove2 :: subjectGoodAt1 :: scoreSubjectGoodAt1 :: subjectGoodAt2 :: scoreSubjectGoodAt2 :: interest1 :: timeInterest1 :: interest2 :: timeInterest2 :: interest3 :: timeInterest3 :: lessonType :: skype :: street :: postalCode :: HNil =>
      if (street.isDefined && postalCode.isDefined)
        TuteeProfile(userID, gender, dob, classLevel, schoolName, learningLanguage, description, TuteeSubjects(subjectImprove1, scoreSubjectImprove1, subjectImprove2, scoreSubjectImprove2, subjectGoodAt1, scoreSubjectGoodAt1, subjectGoodAt2, scoreSubjectGoodAt2), TuteeInterests(interest1, timeInterest1, interest2, timeInterest2, interest3, timeInterest3), lessonType, skype, Some(Address(street.get, postalCode.get)))
      else
        TuteeProfile(userID, gender, dob, classLevel, schoolName, learningLanguage, description, TuteeSubjects(subjectImprove1, scoreSubjectImprove1, subjectImprove2, scoreSubjectImprove2, subjectGoodAt1, scoreSubjectGoodAt1, subjectGoodAt2, scoreSubjectGoodAt2), TuteeInterests(interest1, timeInterest1, interest2, timeInterest2, interest3, timeInterest3), lessonType, skype, None)
  }

  def unapply(tuteeProfile: TuteeProfile): Option[TuteeProfileHList] = tuteeProfile match {

    case TuteeProfile(userID, gender, dob, classLevel, schoolName, learningLanguage, description, TuteeSubjects(subjectImprove1, scoreSubjectImprove1, subjectImprove2, scoreSubjectImprove2, subjectGoodAt1, scoreSubjectGoodAt1, subjectGoodAt2, scoreSubjectGoodAt2), TuteeInterests(interest1, timeInterest1, interest2, timeInterest2, interest3, timeInterest3), lessonType, skype, None) =>
      Some(userID :: gender :: dob :: classLevel :: schoolName :: learningLanguage :: description :: subjectImprove1 :: scoreSubjectImprove1 :: subjectImprove2 :: scoreSubjectImprove2 :: subjectGoodAt1 :: scoreSubjectGoodAt1 :: subjectGoodAt2 :: scoreSubjectGoodAt2 :: interest1 :: timeInterest1 :: interest2 :: timeInterest2 :: interest3 :: timeInterest3 :: lessonType :: skype :: None :: None :: HNil)
    case TuteeProfile(userID, gender, dob, classLevel, schoolName, learningLanguage, description, TuteeSubjects(subjectImprove1, scoreSubjectImprove1, subjectImprove2, scoreSubjectImprove2, subjectGoodAt1, scoreSubjectGoodAt1, subjectGoodAt2, scoreSubjectGoodAt2), TuteeInterests(interest1, timeInterest1, interest2, timeInterest2, interest3, timeInterest3), lessonType, skype, Some(Address(street, postalCode))) =>
      Some(userID :: gender :: dob :: classLevel :: schoolName :: learningLanguage :: description :: subjectImprove1 :: scoreSubjectImprove1 :: subjectImprove2 :: scoreSubjectImprove2 :: subjectGoodAt1 :: scoreSubjectGoodAt1 :: subjectGoodAt2 :: scoreSubjectGoodAt2 :: interest1 :: timeInterest1 :: interest2 :: timeInterest2 :: interest3 :: timeInterest3 :: lessonType :: skype :: Some(street) :: Some(postalCode) :: HNil)

  }
}
