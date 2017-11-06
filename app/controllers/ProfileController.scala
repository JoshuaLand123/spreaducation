package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import forms.ProfileForm
import models.services.UserService
import org.webjars.play.WebJarsUtil
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, ControllerComponents }
import utils.auth.DefaultEnv

import scala.concurrent.{ ExecutionContext, Future }

class ProfileController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv]
)(
  implicit
  webJarsUtil: WebJarsUtil,
  userService: UserService,
  assets: AssetsFinder,
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def view = silhouette.SecuredAction.async { implicit request =>
    userService.retrieveProfile(request.identity.userID).map(p => Ok(views.html.profile(request.identity, p)))
  }

  def edit = silhouette.SecuredAction.async { implicit request =>
    userService.retrieveProfile(request.identity.userID).map(profile => {
      val form = profile match {
        case Some(p) => ProfileForm.form.fill(p)
        case _       => ProfileForm.form
      }
      Ok(views.html.profileEdit(form, request.identity))
    })
  }

  def submit = silhouette.SecuredAction.async { implicit request =>

    Future.successful(ProfileForm.form.bindFromRequest.fold(
      errors =>
        Redirect(routes.ApplicationController.index).flashing("error" -> s"Something went wrong: ${errors.toString}"),
      profileSuccess => {
        val profile = profileSuccess.copy(userID = request.identity.userID)
        userService.saveProfile(profile)
        Redirect(routes.ProfileController.view())
      }
    ))
  }

}
