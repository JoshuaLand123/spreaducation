package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import helpers.TutorGenerator
import models.services.UserService
import org.webjars.play.WebJarsUtil
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, ControllerComponents }
import utils.auth.DefaultEnv

import scala.concurrent.{ ExecutionContext, Future }

class MatchingController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv],
  userService: UserService
)(
  implicit
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder,
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def view = silhouette.SecuredAction.async { implicit request =>
    val messages = request.messages
    userService.retrieveTuteeProfile(request.identity.userID).flatMap {
      case Some(profile) => {
        userService.findMatches(profile, messages).map {
          case Nil     => Ok(views.html.matching(request.identity, TutorGenerator.generateTutors(profile, messages)))
          case matches => Ok(views.html.matching(request.identity, matches))
        }
      }
      case None => Future.successful(Redirect(routes.TuteeProfileController.edit()).flashing("error" -> "Please fill your profile first"))
    }

  }

}
