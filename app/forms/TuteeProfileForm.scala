package forms

import java.util.UUID

import models.{ Address, TuteeInterests, TuteeProfile, TuteeSubjects }
import play.api.data.Form
import play.api.data.Forms._

object TuteeProfileForm {

  val form = Form(
    mapping(
      "userID" -> ignored(UUID.randomUUID()),
      "gender" -> nonEmptyText,
      "dob" -> date,
      "classLevel" -> number(min = 1, max = 14),
      "schoolName" -> nonEmptyText,
      "mainLanguage" -> nonEmptyText,
      "description" -> nonEmptyText,
      "subjects" -> mapping(
        "subjectImprove1" -> nonEmptyText,
        "scoreSubjectImprove1" -> number(min = 1, max = 6),
        "subjectImprove2" -> nonEmptyText,
        "scoreSubjectImprove2" -> number(min = 1, max = 6),
        "subjectGoodAt1" -> nonEmptyText,
        "scoreSubjectGoodAt1" -> number(min = 1, max = 6),
        "subjectGoodAt2" -> nonEmptyText,
        "scoreSubjectGoodAt2" -> number(min = 1, max = 6)
      )(TuteeSubjects.apply)(TuteeSubjects.unapply),
      "interests" -> mapping(
        "interest1" -> nonEmptyText,
        "timeInterest1" -> number(min = 1, max = 12),
        "interest2" -> nonEmptyText,
        "timeInterest2" -> number(min = 1, max = 12),
        "interest3" -> nonEmptyText,
        "timeInterest3" -> number(min = 1, max = 12)
      )(TuteeInterests.apply)(TuteeInterests.unapply),
      "lessonType" -> nonEmptyText,
      "skype" -> optional(text),
      "address" -> optional(mapping(
        "street" -> text,
        "postalCode" -> text
      )(Address.apply)(Address.unapply))
    )(TuteeProfile.apply)(TuteeProfile.unapply)
  )
}
