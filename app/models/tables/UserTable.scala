package models.tables

import java.util.UUID

import models.enums.UserType.UserType
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

case class DbUser(
  userID: UUID,
  firstName: Option[String],
  lastName: Option[String],
  fullName: Option[String],
  email: Option[String],
  avatarUrl: Option[String],
  userType: UserType,
  activated: Boolean
)

case class DbLoginInfo(
  id: Option[Long],
  providerId: String,
  providerKey: String
)

case class DbUserLoginInfo(
  userId: UUID,
  loginInfoId: Long
)

case class DbPasswordInfo(
  hasher: String,
  password: String,
  salt: Option[String],
  loginInfoId: Long
)

case class DbOAuth1Info(
  id: Option[Long],
  token: String,
  secret: String,
  loginInfoId: Long
)

case class DbOAuth2Info(
  id: Option[Long],
  accessToken: String,
  tokenType: Option[String],
  expiresIn: Option[Int],
  refreshToken: Option[String],
  loginInfoId: Long
)

case class DbOpenIDInfo(
  id: String,
  loginInfoId: Long
)

case class DbOpenIDAttribute(
  id: String,
  key: String,
  value: String
)

class UserTable(tag: Tag) extends Table[DbUser](tag, "users") {

  def userID = column[UUID]("user_id", O.PrimaryKey)

  def firstName = column[Option[String]]("first_name")

  def lastName = column[Option[String]]("last_name")

  def fullName = column[Option[String]]("full_name")

  def email = column[Option[String]]("email")

  def avatarURL = column[Option[String]]("avatar_url")

  def activated = column[Boolean]("activated")

  def userType = column[UserType]("user_type")

  override def * = (userID, firstName, lastName, fullName, email, avatarURL, userType, activated) <> (DbUser.tupled, DbUser.unapply)

}

class LoginInfoTable(tag: Tag) extends Table[DbLoginInfo](tag, "login_info") {

  def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  def providerId = column[String]("provider_id")

  def providerKey = column[String]("provider_key")

  override def * : ProvenShape[DbLoginInfo] = (id, providerId, providerKey) <> (DbLoginInfo.tupled, DbLoginInfo.unapply)

}

class UserLoginInfoTable(tag: Tag) extends Table[DbUserLoginInfo](tag, "user_login_info") {

  def userId = column[UUID]("user_id")

  def loginInfoId = column[Long]("login_info_id")

  def * : ProvenShape[DbUserLoginInfo] = (userId, loginInfoId) <> (DbUserLoginInfo.tupled, DbUserLoginInfo.unapply)

}

class PasswordInfoTable(tag: Tag) extends Table[DbPasswordInfo](tag, "password_info") {

  def hasher = column[String]("hasher")

  def password = column[String]("password")

  def salt = column[Option[String]]("salt")

  def loginInfoId = column[Long]("login_info_id")

  def * : ProvenShape[DbPasswordInfo] = (hasher, password, salt, loginInfoId) <> (DbPasswordInfo.tupled, DbPasswordInfo.unapply)

}

class OAuth1InfoTable(tag: Tag) extends Table[DbOAuth1Info](tag, "oauth1_info") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def token = column[String]("token")

  def secret = column[String]("secret")

  def loginInfoId = column[Long]("login_info_id")

  def * : ProvenShape[DbOAuth1Info] = (id.?, token, secret, loginInfoId) <> (DbOAuth1Info.tupled, DbOAuth1Info.unapply)

}

class OAuth2InfoTable(tag: Tag) extends Table[DbOAuth2Info](tag, "oauth2_info") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def accessToken = column[String]("access_token")

  def tokenType = column[Option[String]]("token_type")

  def expiresIn = column[Option[Int]]("expires_in")

  def refreshToken = column[Option[String]]("refresh_token")

  def loginInfoId = column[Long]("login_info_id")

  def * : ProvenShape[DbOAuth2Info] = (id.?, accessToken, tokenType, expiresIn, refreshToken, loginInfoId) <> (DbOAuth2Info.tupled, DbOAuth2Info.unapply)

}

class OpenIDInfoTable(tag: Tag) extends Table[DbOpenIDInfo](tag, "open_id_info") {

  def id = column[String]("id", O.PrimaryKey)

  def loginInfoId = column[Long]("login_info_id")

  def * : ProvenShape[DbOpenIDInfo] = (id, loginInfoId) <> (DbOpenIDInfo.tupled, DbOpenIDInfo.unapply)

}

class OpenIDAttributeTable(tag: Tag) extends Table[DbOpenIDAttribute](tag, "open_id_attributes") {

  def id = column[String]("id")

  def key = column[String]("key")

  def value = column[String]("value")

  def * : ProvenShape[DbOpenIDAttribute] = (id, key, value) <> (DbOpenIDAttribute.tupled, DbOpenIDAttribute.unapply)

}