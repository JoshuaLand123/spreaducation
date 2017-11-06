package forms

import models.UserProfile
import play.api.data.Form
import play.api.data.Forms._

object ProfileForm {

  /**
   * A play framework form.
   */
  val form = Form(
    mapping(
      "userID" -> uuid,
      "gender" -> nonEmptyText,
      "dob" -> date(pattern = "yyyy-MM-dd"),
      "classLevel" -> number(min = 5, max = 12),
      "schoolName" -> nonEmptyText,
      "mainLanguage" -> nonEmptyText,
      "subjectImprove" -> nonEmptyText,
      "scoreSubjectImprove" -> number(min = 0, max = 6),
      "subjectGoodAt" -> nonEmptyText,
      "scoreSubjectGoodAt" -> number(min = 0, max = 6),
      "interest1" -> nonEmptyText,
      "timeInterest1" -> number(min = 1, max = 12),
      "interest2" -> nonEmptyText,
      "timeInterest2" -> number(min = 1, max = 12),
      "interest3" -> nonEmptyText,
      "timeInterest3" -> number(min = 1, max = 12)
    )(UserProfile.apply)(UserProfile.unapply)
  )
}
