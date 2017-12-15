package forms

import java.util.UUID

import models.{ TutorInterests, TutorProfile, TutorSubjects }
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._

object TutorProfileForm {

  val form = Form(
    mapping(
      "userID" -> ignored(UUID.randomUUID()),
      "gender" -> nonEmptyText,
      "dob" -> date,
      "mainLanguage" -> nonEmptyText,
      "description" -> nonEmptyText,
      "instituteAttended" -> nonEmptyText,
      "occupation" -> nonEmptyText,
      "occupationDegree" -> nonEmptyText,
      "workingLanguage" -> nonEmptyText,
      "subjects" -> mapping(
        "subject1" -> nonEmptyText,
        "subject1Level" -> number,
        "subject2" -> optional(text),
        "subject2Level" -> optional(number),
        "subject3" -> optional(text),
        "subject3Level" -> optional(number),
        "subject4" -> optional(text),
        "subject4Level" -> optional(number)
      )(TutorSubjects.apply)(TutorSubjects.unapply),
      "interests" -> mapping(
        "interest1" -> nonEmptyText,
        "timeInterest1" -> number(min = 1, max = 12),
        "interest2" -> nonEmptyText,
        "timeInterest2" -> number(min = 1, max = 12),
        "interest3" -> nonEmptyText,
        "timeInterest3" -> number(min = 1, max = 12)
      )(TutorInterests.apply)(TutorInterests.unapply),
      "wishedSalary" -> of(doubleFormat),
      "lessonType" -> nonEmptyText,
      "place" -> optional(text),
      "image" -> ignored(Option(Array[Byte]()))
    )(TutorProfile.apply)(TutorProfile.unapply)
  )
}
