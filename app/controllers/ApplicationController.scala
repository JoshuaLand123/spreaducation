package controllers

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{LogoutEvent, Silhouette}
import models.enums.UserType
import models.services.UserService
import org.webjars.play.WebJarsUtil
import play.api.i18n._
import play.api.mvc._
import utils.auth.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

class ApplicationController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv],
  userService: UserService,
)(
  implicit
  webJarsUtil: WebJarsUtil,
  assetsFinder: AssetsFinder,
  assets: Assets,
  messagesApi: MessagesApi,
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def index = silhouette.SecuredAction.async { implicit request =>
    if (request.identity.userType == UserType.Tutee)
      Future.successful(Redirect(routes.TuteeProfileController.view()))
    else
      Future.successful(Redirect(routes.TutorProfileController.view()))
  }

  def changeLanguage(language: String) = Action { implicit request =>
    Redirect(routes.ApplicationController.index()).withLang(Lang(language))
  }

  def signOut = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    val result = Redirect(routes.ApplicationController.index())
    silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    silhouette.env.authenticatorService.discard(request.authenticator, result)
  }

  def getImage = silhouette.SecuredAction.async { implicit request =>
    request.identity.image match {
      case Some(image) => Future.successful(Ok(image).as("image/jpg"))
      case None => assets.at("/public/images/", "yes.png").apply(request)
    }
  }

  def image(userId: UUID) = silhouette.SecuredAction.async { implicit request =>
    userService.retrieve(userId).map(_.flatMap(g => g.image)).flatMap {
      case Some(image) => Future.successful(Ok(image).as("image/jpg"))
      case None  => assets.at("/public/images/", "yes.png").apply(request)
    }
  }

}
