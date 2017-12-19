package forms

import java.util.UUID

import models.TuteeProfile
import play.api.data.Form
import play.api.data.Forms._

object TuteeProfileForm {

  val form = Form(
    mapping(
      "userID" -> ignored(UUID.randomUUID()),
      "gender" -> nonEmptyText,
      "dob" -> date,
      "classLevel" -> number(min = 7, max = 13),
      "schoolName" -> nonEmptyText,
      "mainLanguage" -> nonEmptyText,
      "subjectImprove1" -> nonEmptyText,
      "scoreSubjectImprove1" -> number(min = 1, max = 6),
      "subjectImprove2" -> nonEmptyText,
      "scoreSubjectImprove2" -> number(min = 1, max = 6),
      "subjectGoodAt1" -> nonEmptyText,
      "scoreSubjectGoodAt1" -> number(min = 1, max = 6),
      "subjectGoodAt2" -> nonEmptyText,
      "scoreSubjectGoodAt2" -> number(min = 1, max = 6),
      "interest1" -> nonEmptyText,
      "timeInterest1" -> number(min = 1, max = 12),
      "interest2" -> nonEmptyText,
      "timeInterest2" -> number(min = 1, max = 12),
      "interest3" -> nonEmptyText,
      "timeInterest3" -> number(min = 1, max = 12),
      "tutorOrder" -> optional(number),
      "tutorOrder" -> ignored(Option(UUID.randomUUID()))
    )(TuteeProfile.apply)(TuteeProfile.unapply)
  )
}
