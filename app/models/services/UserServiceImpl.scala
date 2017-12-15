package models.services
import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import models.{ TuteeProfile, TutorProfile, User }
import models.daos.UserDAO
import models.enums.UserType

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Handles actions to users.
 *
 * @param userDAO The user DAO implementation.
 */
class UserServiceImpl @Inject() (userDAO: UserDAO) extends UserService {

  override def retrieve(id: UUID) = userDAO.find(id)

  override def retrieve(loginInfo: LoginInfo): Future[Option[User]] = userDAO.find(loginInfo)

  override def save(user: User) = userDAO.save(user)

  override def save(profile: CommonSocialProfile) = {
    userDAO.find(profile.loginInfo).flatMap {
      case Some(user) => // Update user with profile
        userDAO.save(user.copy(
          firstName = profile.firstName,
          lastName = profile.lastName,
          fullName = profile.fullName,
          email = profile.email,
          avatarURL = profile.avatarURL
        ))
      case None => // Insert a new user
        userDAO.save(User(
          userID = UUID.randomUUID(),
          loginInfo = profile.loginInfo,
          firstName = profile.firstName,
          lastName = profile.lastName,
          fullName = profile.fullName,
          email = profile.email,
          avatarURL = profile.avatarURL,
          userType = UserType.Tutee,
          activated = true
        ))
    }
  }

  override def retrieveTuteeProfile(userID: UUID) = userDAO.findTuteeProfile(userID)

  override def retrieveTutorProfile(userID: UUID) = userDAO.findTutorProfile(userID)

  override def saveTuteeProfile(profile: TuteeProfile) = userDAO.saveTuteeProfile(profile: TuteeProfile)

  override def saveTutorProfile(profile: TutorProfile) = userDAO.saveTutorProfile(profile: TutorProfile)
}