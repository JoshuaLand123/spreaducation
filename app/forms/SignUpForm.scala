package forms

import models.enums.UserType
import models.enums.UserType.UserType
import play.api.data.{Form, FormError, Forms}
import play.api.data.Forms._
import play.api.data.format.Formatter

/**
 * The form which handles the sign up process.
 */
object SignUpForm {

  implicit def userTypeFormatter: Formatter[UserType] = new Formatter[UserType] {

    override def bind(key: String, data: Map[String, String]) =
      data.get(key)
        .map(UserType.withName)
        .toRight(Seq(FormError(key, "error.required", Nil)))

    override def unbind(key: String, value: UserType) =
      Map(key -> value.toString)
  }

  /**
   * A play framework form.
   */
  val form = Form(
    mapping(
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "email" -> email,
      "password" -> nonEmptyText,
      "userType" -> Forms.of[UserType],
    )(Data.apply)(Data.unapply)
  )

  /**
   * The form data.
   *
   * @param firstName The first name of a user.
   * @param lastName The last name of a user.
   * @param email The email of the user.
   * @param password The password of the user.
   */
  case class Data(
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    userType: UserType
  )
}
