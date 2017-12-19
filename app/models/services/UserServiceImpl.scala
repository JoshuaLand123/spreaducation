package models.services
import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import models.{ TuteeProfile, TutorMatch, TutorMatchDB, TutorProfile, User }
import models.daos.UserDAO
import models.enums.UserType
import play.api.i18n.Messages

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

  override def saveTuteeProfile(profile: TuteeProfile) = userDAO.saveTuteeProfile(profile)

  override def saveTutorProfile(profile: TutorProfile) = userDAO.saveTutorProfile(profile)

  override def findMatches(profile: TuteeProfile, messages: Messages) = {
    def mapToStatus(wishedSalary: Double): String =
      if (wishedSalary < 30) "Frischling"
      else if (wishedSalary < 40) "Fortgeschritten"
      else if (wishedSalary < 50) "Halb-Profi"
      else if (wishedSalary < 60) "Profi"
      else "Legende"

    def convert(tutorMatchDB: TutorMatchDB): TutorMatch = {
      val subjects = Seq(
        (Some(tutorMatchDB.subject1), Some(tutorMatchDB.subject1Level)),
        (tutorMatchDB.subject2, tutorMatchDB.subject2Level),
        (tutorMatchDB.subject3, tutorMatchDB.subject3Level),
        (tutorMatchDB.subject4, tutorMatchDB.subject4Level)
      ).collect {
          case (Some(subject), Some(level)) if level >= profile.classLevel
            && Seq(profile.subjectImprove1, profile.subjectImprove2).contains(subject) => subject
        }.take(2)

      val subject1 = subjects.headOption.map(s => messages(s"subject.$s")).getOrElse("")
      val subject2 = if (subjects.size > 1) Some(messages(s"subject.${subjects.tail.head}")) else None
      val interest = messages(s"interest.${Seq(Some(tutorMatchDB.interest1), tutorMatchDB.interest2, tutorMatchDB.interest3).flatten.find(Seq(profile.interest1, profile.interest2, profile.interest3).contains).getOrElse(tutorMatchDB.interest1)}")
      TutorMatch(
        userID = tutorMatchDB.userID,
        firstName = tutorMatchDB.firstName,
        lastName = tutorMatchDB.lastName,
        description = tutorMatchDB.description,
        avatarUrl = tutorMatchDB.avatarUrl,
        price = tutorMatchDB.price * 1.2,
        subject1 = subject1,
        subject2 = subject2,
        interest = interest,
        matchingScore = 90,
        status = mapToStatus(tutorMatchDB.price),
        order = 1,
        isFake = false
      )
    }

    userDAO.findMatches(profile.userID).map(_.map(convert).filter(_.subject1 != "").sortBy(-_.price).zipWithIndex.map { case (a, order) => a.copy(order = order + 1, matchingScore = 100 - (order + 1) * 5 + order) })
  }
}