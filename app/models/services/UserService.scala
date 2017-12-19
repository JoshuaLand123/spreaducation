package models.services

import java.util.UUID

import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import models.{ TuteeProfile, TutorMatch, TutorProfile, User }

import scala.concurrent.Future

trait UserService extends IdentityService[User] {

  def retrieve(id: UUID): Future[Option[User]]

  def save(user: User): Future[User]

  def save(profile: CommonSocialProfile): Future[User]

  def retrieveTuteeProfile(userID: UUID): Future[Option[TuteeProfile]]

  def retrieveTutorProfile(userID: UUID): Future[Option[TutorProfile]]

  def saveTuteeProfile(profile: TuteeProfile): Future[Int]

  def saveTutorProfile(profile: TutorProfile): Future[Int]

  def findMatches(profile: TuteeProfile): Future[Seq[TutorMatch]]
}
