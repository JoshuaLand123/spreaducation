package models.services

import java.util.UUID

import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import models.{ User, UserProfile }

import scala.concurrent.Future

trait UserService extends IdentityService[User] {

  def retrieve(id: UUID): Future[Option[User]]

  def save(user: User): Future[User]

  def save(profile: CommonSocialProfile): Future[User]

  def retrieveProfile(userID: UUID): Future[Option[UserProfile]]

  def saveProfile(profile: UserProfile): Future[Int]
}
