package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import forms.ProfileForm
import models.services.UserService
import org.webjars.play.WebJarsUtil
import play.api.i18n._
import play.api.libs.json.Json
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

    userService.retrieveProfile(request.identity.userID).map {
      case Some(p) =>
        val messages: Messages = request.messages
        val map = Map[String, List[(String, Int)]]()

        val interests = "interests" -> List((messages("interest." + p.interest1), p.timeInterest1), (messages("interest." + p.interest2), p.timeInterest2), (messages("interest." + p.interest3), p.timeInterest3))
        val surroundings = "surroundings" -> List(("", 0), ("", 0), ("", 0), ("", 0))
        val constitution = "constitution" -> List(("", 0), ("", 0), ("", 0), ("", 0))
        val expertise = "expertise" -> List((messages("subject." + p.subjectGoodAt1), p.scoreSubjectGoodAt1), (messages("subject." + p.subjectGoodAt2), p.scoreSubjectGoodAt2), (messages("subject." + p.subjectImprove1), p.scoreSubjectImprove1), (messages("subject." + p.subjectImprove2), p.scoreSubjectImprove2))
        val operation = "operation" -> List(("", 0), ("", 0), ("", 0))

        val psychogramDataJson = Json.stringify(Json.toJson(map + interests + surroundings + constitution + expertise + operation))

        Ok(views.html.profile(request.identity, Some(p), psychogramDataJson))
      case None => Ok(views.html.profile(request.identity, None, "{}"))

    }

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
        //Redirect(routes.ApplicationController.index).flashing("error" -> s"Error: $errors"),
        Redirect(routes.ProfileController.edit).flashing("error" -> s"Error: ${errors.toString}"),
      profileSuccess => {
        val profile = profileSuccess.copy(userID = request.identity.userID)
        userService.saveProfile(profile)
        //Redirect(routes.ProfileController.view())
        Redirect(routes.QuestionsController.index)
      }
    ))
  }

}
