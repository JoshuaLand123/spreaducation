package forms

import java.util.UUID

import models.TutorProfile
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
      "subjectAbleToTeach" -> nonEmptyText,
      "interest1" -> nonEmptyText,
      "timeInterest1" -> number(min = 1, max = 12),
      "interest2" -> nonEmptyText,
      "timeInterest2" -> number(min = 1, max = 12),
      "interest3" -> nonEmptyText,
      "timeInterest3" -> number(min = 1, max = 12),
      "wishedSalary" -> of(doubleFormat),
      "lessonType" -> nonEmptyText
    )(TutorProfile.apply)(TutorProfile.unapply)
  )
}
