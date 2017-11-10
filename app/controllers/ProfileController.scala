package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import forms.ProfileForm
import models.UserProfile
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
        Ok(views.html.profile(request.identity, Some(p), psychogramDataJsonString(p, request.messages)))
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

  private def psychogramDataJsonString(profile: UserProfile, messages: Messages): String = {

    val expertise = {
      def reverseScore(score: Int) = score match {
        case 1 => 5
        case 2 => 4
        case 3 => 3
        case 4 => 2
        case 5 => 1
        case 6 => 1
      }

      "subjects" -> List(
        (messages("subject." + profile.subjectGoodAt1), reverseScore(profile.scoreSubjectGoodAt1)),
        (messages("subject." + profile.subjectGoodAt2), reverseScore(profile.scoreSubjectGoodAt2)),
        (messages("subject." + profile.subjectImprove1), reverseScore(profile.scoreSubjectImprove1)),
        (messages("subject." + profile.subjectImprove2), reverseScore(profile.scoreSubjectImprove2))
      )
    }

    val interests = "interests" -> List(
      (messages("interest." + profile.interest1), profile.timeInterest1),
      (messages("interest." + profile.interest2), profile.timeInterest2),
      (messages("interest." + profile.interest3), profile.timeInterest3)
    )
    val environment = "environment" -> List(("", 0), ("", 0), ("", 0), ("", 0))
    val constitution = "constitution" -> List(("", 0), ("", 0), ("", 0), ("", 0))
    val working = "working" -> List(("", 0), ("", 0), ("", 0))

    Json.stringify(Json.toJson(Map(interests, environment, constitution, expertise, working)))
  }

}
