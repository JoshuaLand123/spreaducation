package models.daos

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import models.{ User, UserProfile }
import models.daos.UserDAOImpl._
import models.tables._
import play.api.db.slick.DatabaseConfigProvider
import slick.basic.DatabaseConfig
import slick.jdbc.{ JdbcBackend, JdbcProfile }
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserDAOImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends UserDAO {

  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db

  import dbConfig.profile.api._

  def loginInfoQuery(loginInfo: LoginInfo): Query[LoginInfoTable, DbLoginInfo, Seq] = {
    loginInfos.filter(dbLoginInfo => dbLoginInfo.providerId === loginInfo.providerID && dbLoginInfo.providerKey === loginInfo.providerKey)
  }

  def find(loginInfo: LoginInfo): Future[Option[User]] = {
    val userQuery = for {
      dbLoginInfo <- loginInfoQuery(loginInfo)
      dbUserLoginInfo <- userLoginInfos.filter(_.loginInfoId === dbLoginInfo.id)
      dbUser <- users.filter(_.userID === dbUserLoginInfo.userId)
    } yield dbUser

    db.run(userQuery.result.headOption).map { dbUserOption =>
      dbUserOption.map {
        user => User(user.userID, loginInfo, user.firstName, user.lastName, user.fullName, user.email, user.avatarUrl, user.userType, user.activated)
      }
    }
  }

  def find(userID: UUID): Future[Option[User]] = {

    val userQuery = for {
      dbUser <- users.filter(_.userID === userID)
      dbUserLoginInfo <- userLoginInfos.filter(_.userId === dbUser.userID)
      dbLoginInfo <- loginInfos.filter(_.id === dbUserLoginInfo.loginInfoId)
    } yield (dbUser, dbLoginInfo)

    db.run(userQuery.result.headOption).map { resultOption =>
      resultOption.map {
        case (user, loginInfo) => User(
          user.userID,
          LoginInfo(loginInfo.providerId, loginInfo.providerKey),
          user.firstName,
          user.lastName,
          user.fullName,
          user.email,
          user.avatarUrl,
          user.userType,
          user.activated
        )
      }
    }
  }

  def save(user: User): Future[User] = {
    val dbUser = DbUser(user.userID, user.firstName, user.lastName, user.fullName, user.email, user.avatarURL, user.userType, user.activated)
    val dbLoginInfo = DbLoginInfo(None, user.loginInfo.providerID, user.loginInfo.providerKey)

    val loginInfoAction = {
      val retrieveLoginInfo = loginInfos.filter(
        info => info.providerId === user.loginInfo.providerID &&
          info.providerKey === user.loginInfo.providerKey
      ).result.headOption

      val insertLoginInfo = loginInfos.returning(loginInfos.map(_.id)).
        into((info, id) => info.copy(id)) += dbLoginInfo

      for {
        loginInfoOption <- retrieveLoginInfo
        loginInfo <- loginInfoOption.map(DBIO.successful).getOrElse(insertLoginInfo)
      } yield loginInfo
    }

    val actions = (for {
      _ <- users.insertOrUpdate(dbUser)
      loginInfo <- loginInfoAction
      _ <- userLoginInfos += DbUserLoginInfo(dbUser.userID, loginInfo.id.get)
    } yield ()).transactionally

    db.run(actions).map(_ => user)
  }

  override def saveProfile(profile: UserProfile) =
    db.run(userProfiles.insertOrUpdate(profile))

  override def findProfile(userID: UUID) =
    db.run(userProfiles.filter(_.userID === userID).result.headOption)
}

object UserDAOImpl {

  private val loginInfos = TableQuery[LoginInfoTable]
  private val users = TableQuery[UserTable]
  private val userLoginInfos = TableQuery[UserLoginInfoTable]
  private val userProfiles = TableQuery[UserProfileTable]

}