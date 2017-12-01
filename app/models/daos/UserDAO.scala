package models.daos

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.{ User, TuteeProfile }
import models.tables.{ DbLoginInfo, LoginInfoTable }
import slick.lifted.Query

import scala.concurrent.Future

trait UserDAO {

  def loginInfoQuery(loginInfo: LoginInfo): Query[LoginInfoTable, DbLoginInfo, Seq]

  def find(loginInfo: LoginInfo): Future[Option[User]]

  def find(userID: UUID): Future[Option[User]]

  def save(user: User): Future[User]

  def saveProfile(profile: TuteeProfile): Future[Int]

  def findProfile(userID: UUID): Future[Option[TuteeProfile]]

}
