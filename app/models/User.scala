package models

import java.util.UUID

import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }
import models.enums.UserType.UserType

case class User(
  userID: UUID,
  loginInfo: LoginInfo,
  firstName: Option[String],
  lastName: Option[String],
  fullName: Option[String],
  email: Option[String],
  avatarURL: Option[String],
  userType: UserType,
  activated: Boolean,
  image: Option[Array[Byte]] = None
) extends Identity {

  /**
   * Tries to construct a name.
   *
   * @return Maybe a name.
   */
  def name = fullName.orElse {
    firstName -> lastName match {
      case (Some(f), Some(l)) => Some(f + " " + l)
      case (Some(f), None)    => Some(f)
      case (None, Some(l))    => Some(l)
      case _                  => None
    }
  }
}
