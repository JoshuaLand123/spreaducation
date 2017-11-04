package controllers

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.{ PasswordHasherRegistry, PasswordInfo }
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import forms.ResetPasswordForm
import models.services.{ AuthTokenService, UserService }
import org.webjars.play.WebJarsUtil
import play.api.i18n.{ I18nSupport, Messages }
import play.api.mvc.{ AbstractController, AnyContent, ControllerComponents, Request }
import utils.auth.DefaultEnv

import scala.concurrent.{ ExecutionContext, Future }

class ResetPasswordController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv],
  userService: UserService,
  authInfoRepository: AuthInfoRepository,
  passwordHasherRegistry: PasswordHasherRegistry,
  authTokenService: AuthTokenService
)(
  implicit
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder,
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def view(token: UUID) = silhouette.UnsecuredAction.async { implicit request: Request[AnyContent] =>
    authTokenService.validate(token).map {
      case Some(_) => Ok(views.html.resetPassword(ResetPasswordForm.form, token))
      case None    => Redirect(routes.SignInController.view()).flashing("error" -> Messages("invalid.reset.link"))
    }
  }

  def submit(token: UUID) = silhouette.UnsecuredAction.async { implicit request: Request[AnyContent] =>
    authTokenService.validate(token).flatMap {
      case Some(authToken) =>
        ResetPasswordForm.form.bindFromRequest.fold(
          form => Future.successful(BadRequest(views.html.resetPassword(form, token))),
          password => userService.retrieve(authToken.userID).flatMap {
            case Some(user) if user.loginInfo.providerID == CredentialsProvider.ID =>
              val passwordInfo = passwordHasherRegistry.current.hash(password)
              authInfoRepository.update[PasswordInfo](user.loginInfo, passwordInfo).map { _ =>
                Redirect(routes.SignInController.view()).flashing("success" -> Messages("password.reset"))
              }
            case _ => Future.successful(Redirect(routes.SignInController.view()).flashing("error" -> Messages("invalid.reset.link")))
          }
        )
      case None => Future.successful(Redirect(routes.SignInController.view()).flashing("error" -> Messages("invalid.reset.link")))
    }
  }
}
