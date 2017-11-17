package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import helpers.TutorGenerator
import models.services.UserService
import org.webjars.play.WebJarsUtil
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, ControllerComponents }
import utils.auth.DefaultEnv

import scala.concurrent.ExecutionContext

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
    userService.retrieveProfile(request.identity.userID).map {
      case Some(p) => Ok(views.html.matching(request.identity, TutorGenerator.generateTutors(p, request.messages)))
      case None    => Redirect(routes.ProfileController.edit()).flashing("error" -> "Please fill your profile first")
    }

  }

}
