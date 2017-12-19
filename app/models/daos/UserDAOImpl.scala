package models.daos

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import models.daos.UserDAOImpl._
import models.tables._
import models.{ TuteeProfile, TutorMatchDB, TutorProfile, User }
import play.api.db.slick.DatabaseConfigProvider
import slick.basic.DatabaseConfig
import slick.jdbc.{ GetResult, JdbcBackend, JdbcProfile }
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
        user => User(user.userID, loginInfo, user.firstName, user.lastName, user.fullName, user.email, user.avatarUrl, user.userType, user.activated, user.image)
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
          user.activated,
          user.image
        )
      }
    }
  }

  def save(user: User): Future[User] = {
    val dbUser = DbUser(user.userID, user.firstName, user.lastName, user.fullName, user.email, user.avatarURL, user.userType, user.activated, user.image)
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

  override def saveTuteeProfile(profile: TuteeProfile) =
    db.run(tuteeProfiles.insertOrUpdate(profile))

  override def saveTutorProfile(profile: TutorProfile) =
    db.run(tutorProfiles.insertOrUpdate(profile))

  override def findTuteeProfile(userID: UUID) =
    db.run(tuteeProfiles.filter(_.userID === userID).result.headOption)

  override def findTutorProfile(userID: UUID) =
    db.run(tutorProfiles.filter(_.userID === userID).result.headOption)

  override def findMatches(userID: UUID): Future[Seq[TutorMatchDB]] = {
    val userIdString = userID.toString
    implicit val getTutorMatchResult = GetResult(r => TutorMatchDB(UUID.fromString(r.nextString()), r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
    val query = sql"""select
                  distinct u.user_id, u.first_name, u.last_name, tutor.description, u.avatar_url, tutor.wished_salary,
                     tutor.subject_1, tutor.subject_1_level, tutor.subject_2, tutor.subject_2_level, tutor.subject_3, tutor.subject_3_level, tutor.subject_4, tutor.subject_4_level,
                     tutor.interest_1, tutor.interest_2, tutor.interest_3 from users u
                     join tutor_profile tutor on tutor.user_id = u.user_id
                     where ARRAY[tutor.subject_1, tutor.subject_2, tutor.subject_3, tutor.subject_4] && (select ARRAY[subject_improve_1, subject_improve_2] from tutee_profile where user_id = '#$userIdString')
                     and u.activated = true;
           """.as[TutorMatchDB]
    db.run(query)
  }
}

object UserDAOImpl {

  private val loginInfos = TableQuery[LoginInfoTable]
  private val users = TableQuery[UserTable]
  private val userLoginInfos = TableQuery[UserLoginInfoTable]
  private val tuteeProfiles = TableQuery[TuteeProfileTable]
  private val tutorProfiles = TableQuery[TutorProfileTable]

}