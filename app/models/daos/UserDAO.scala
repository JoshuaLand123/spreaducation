package models.daos

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.tables.{ DbLoginInfo, LoginInfoTable }
import models.{ TuteeProfile, TutorMatchDB, TutorProfile, User }
import slick.lifted.Query

import scala.concurrent.Future

trait UserDAO {

  def loginInfoQuery(loginInfo: LoginInfo): Query[LoginInfoTable, DbLoginInfo, Seq]

  def find(loginInfo: LoginInfo): Future[Option[User]]

  def find(userID: UUID): Future[Option[User]]

  def save(user: User): Future[User]

  def saveTuteeProfile(profile: TuteeProfile): Future[Int]

  def saveTutorProfile(profile: TutorProfile): Future[Int]

  def findTuteeProfile(userID: UUID): Future[Option[TuteeProfile]]

  def findTutorProfile(userID: UUID): Future[Option[TutorProfile]]

  def findMatches(userID: UUID): Future[Seq[TutorMatchDB]]

}
