package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{ LogoutEvent, Silhouette }
import models.services.UserService
import forms.TuteeProfileForm
import org.webjars.play.WebJarsUtil
import play.api.i18n._
import play.api.mvc._
import utils.auth.DefaultEnv

import scala.concurrent.{ ExecutionContext, Future }

class ApplicationController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv],
  userService: UserService
)(
  implicit
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder,
  messagesApi: MessagesApi,
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def index = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Redirect(routes.TuteeProfileController.view()))
  }

  def changeLanguage(language: String) = Action { implicit request =>
    Redirect(routes.ApplicationController.index()).withLang(Lang(language))
  }

  def signOut = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    val result = Redirect(routes.ApplicationController.index())
    silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    silhouette.env.authenticatorService.discard(request.authenticator, result)
  }

  def feedback(order: Int) = silhouette.SecuredAction.async { implicit request =>
    userService.retrieveTuteeProfile(request.identity.userID).map(_.map(p => if (p.tutorOrder.isEmpty) userService.saveTuteeProfile(p.copy(tutorOrder = Some(order)))))
    Future.successful(Ok(views.html.feedback(request.identity)))
  }
}
