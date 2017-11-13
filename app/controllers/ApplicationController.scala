package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{ LogoutEvent, Silhouette }
import org.webjars.play.WebJarsUtil
import play.api.i18n._
import play.api.mvc._
import utils.auth.DefaultEnv

import scala.concurrent.Future

class ApplicationController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv]
)(
  implicit
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder,
  messagesApi: MessagesApi
) extends AbstractController(components) with I18nSupport {

  def index = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    //Future.successful(Ok(views.html.home(request.identity)))
    Future.successful(Redirect(routes.ProfileController.view()))
  }

  def changeLanguage(language: String) = Action { implicit request =>
    Redirect(routes.ApplicationController.index()).withLang(Lang(language))
  }

  def signOut = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    val result = Redirect(routes.ApplicationController.index())
    silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    silhouette.env.authenticatorService.discard(request.authenticator, result)
  }

  def feedback = silhouette.SecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.feedback(request.identity)))
  }
}
