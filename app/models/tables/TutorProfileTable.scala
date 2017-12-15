package models
package tables

import java.util.{ Date, UUID }

import slick.jdbc.PostgresProfile.api._
import slick.collection.heterogeneous._
import slick.collection.heterogeneous.syntax._

class TutorProfileTable(tag: Tag) extends Table[TutorProfile](tag, "tutor_profile") {

  def userID = column[UUID]("user_id", O.PrimaryKey)
  def gender = column[String]("gender")
  def dob = column[Date]("dob")
  def mainLanguage = column[String]("main_language")
  def description = column[String]("description")
  def instituteAttended = column[String]("institute_attended")
  def occupation = column[String]("occupation")
  def occupationDegree = column[String]("occupation_degree")
  def workingLanguage = column[String]("working_language")
  def subject1 = column[String]("subject_1")
  def subject1Level = column[Int]("subject_1_level")
  def subject2 = column[Option[String]]("subject_2")
  def subject2Level = column[Option[Int]]("subject_2_level")
  def subject3 = column[Option[String]]("subject_3")
  def subject3Level = column[Option[Int]]("subject_3_level")
  def subject4 = column[Option[String]]("subject_4")
  def subject4Level = column[Option[Int]]("subject_4_level")
  def interest1 = column[String]("interest_1")
  def timeInterest1 = column[Int]("time_interest_1")
  def interest2 = column[String]("interest_2")
  def timeInterest2 = column[Int]("time_interest_2")
  def interest3 = column[String]("interest_3")
  def timeInterest3 = column[Int]("time_interest_3")
  def wishedSalary = column[Double]("wished_salary")
  def lessonType = column[String]("lesson_type")
  def place = column[Option[String]]("place")
  def imageByte = column[Option[Array[Byte]]]("image_byte")

  override def * = (userID :: gender :: dob :: mainLanguage :: description :: instituteAttended :: occupation :: occupationDegree :: workingLanguage :: subject1 :: subject1Level :: subject2 :: subject2Level :: subject3 :: subject3Level :: subject4 :: subject4Level :: interest1 :: timeInterest1 :: interest2 :: timeInterest2 :: interest3 :: timeInterest3 :: wishedSalary :: lessonType :: place :: imageByte :: HNil) <> (tuple, unapply)

  type TutorProfileHList = UUID :: String :: Date :: String :: String :: String :: String :: String :: String :: String :: Int :: Option[String] :: Option[Int] :: Option[String] :: Option[Int] :: Option[String] :: Option[Int] :: String :: Int :: String :: Int :: String :: Int :: Double :: String :: Option[String] :: Option[Array[Byte]] :: HNil

  def tuple(data: TutorProfileHList): TutorProfile = data match {

    case userID :: gender :: dob :: mainLanguage :: description :: instituteAttended :: occupation :: occupationDegree :: workingLanguage :: subject1 :: subject1Level :: subject2 :: subject2Level :: subject3 :: subject3Level :: subject4 :: subject4Level :: interest1 :: timeInterest1 :: interest2 :: timeInterest2 :: interest3 :: timeInterest3 :: wishedSalary :: lessonType :: place :: imageByte :: HNil =>
      TutorProfile(userID, gender, dob, mainLanguage, description, instituteAttended, occupation, occupationDegree, workingLanguage, TutorSubjects(subject1, subject1Level, subject2, subject2Level, subject3, subject3Level, subject4, subject4Level), TutorInterests(interest1, timeInterest1, interest2, timeInterest2, interest3, timeInterest3), wishedSalary, lessonType, place, imageByte)
  }

  def unapply(tutorProfile: TutorProfile): Option[TutorProfileHList] = tutorProfile match {

    case TutorProfile(userID, gender, dob, mainLanguage, description, instituteAttended, occupation, occupationDegree, workingLanguage, TutorSubjects(subject1, subject1Level, subject2, subject2Level, subject3, subject3Level, subject4, subject4Level), TutorInterests(interest1, timeInterest1, interest2, timeInterest2, interest3, timeInterest3), wishedSalary, lessonType, place, imageByte) =>
      Some(userID :: gender :: dob :: mainLanguage :: description :: instituteAttended :: occupation :: occupationDegree :: workingLanguage :: subject1 :: subject1Level :: subject2 :: subject2Level :: subject3 :: subject3Level :: subject4 :: subject4Level :: interest1 :: timeInterest1 :: interest2 :: timeInterest2 :: interest3 :: timeInterest3 :: wishedSalary :: lessonType :: place :: imageByte :: HNil)
  }
}
