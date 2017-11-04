package forms

import models.UserProfile
import play.api.data.Form
import play.api.data.Forms._

/**
 * The form which handles the sign up process.
 */
object ProfileForm {

  /**
   * A play framework form.
   */
  val form = Form(
    mapping(
      "userID" -> uuid,
      "gender" -> nonEmptyText,
      "dob" -> date,
      "classLevel" -> number,
      "schoolName" -> nonEmptyText,
      "mainLanguage" -> nonEmptyText,
      "subjectImprove1" -> nonEmptyText,
      "scoreSubjectImprove1" -> number,
      "interest1" -> nonEmptyText,
      "timeInterest1" -> nonEmptyText,
      "subjectGoodAt1" -> nonEmptyText,
      "scoreSubjectGoodAt1" -> number
    )(UserProfile.apply)(UserProfile.unapply)
  )
}
