package models.tables

import java.util.UUID

import slick.jdbc.PostgresProfile.api._

case class DbAuthToken(
  id: UUID,
  userID: UUID,
  expiry: String
)

class AuthTokenTable(tag: Tag) extends Table[DbAuthToken](tag, "auth_tokens") {

  def id = column[UUID]("id", O.PrimaryKey)

  def userID = column[UUID]("user_id")

  def expiry = column[String]("expiry")

  override def * = (id, userID, expiry) <> (DbAuthToken.tupled, DbAuthToken.unapply)

}