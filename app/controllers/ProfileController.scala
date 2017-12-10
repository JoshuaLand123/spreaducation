package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import forms.TuteeProfileForm
import models.TuteeProfile
import models.services.{ QuestionService, UserService }
import modules.{ PsychogramCategoryData, PsychogramData, PsychogramSubcategoryData }
import org.webjars.play.WebJarsUtil
import play.api.i18n._
import play.api.libs.json._
import play.api.mvc.{ AbstractController, ControllerComponents }
import utils.auth.DefaultEnv

import scala.concurrent.{ ExecutionContext, Future }

class ProfileController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv],
  userService: UserService
)(
  implicit
  webJarsUtil: WebJarsUtil,
  questionService: QuestionService,
  assets: AssetsFinder,
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def view = silhouette.SecuredAction.async { implicit request =>

    userService.retrieveProfile(request.identity.userID).flatMap {
      case Some(p) =>
        questionService.getPsychoSubcategoryScores(request.identity.userID).map(psychoResult =>
          Ok(views.html.profile(request.identity, Some(p), psychogramDataJsonString(p, psychoResult, request.messages))))
      case None => Future.successful(Redirect(routes.ProfileController.edit()))
    }
  }

  def edit = silhouette.SecuredAction.async { implicit request =>
    userService.retrieveProfile(request.identity.userID).map(profile => {
      val form = profile.map(TuteeProfileForm.form.fill).getOrElse(TuteeProfileForm.form)
      Ok(views.html.profileEdit(form, request.identity))
    })
  }

  def submit = silhouette.SecuredAction.async { implicit request =>

    val messages = request.messages
    Future.successful(TuteeProfileForm.form.bindFromRequest.fold(
      errors =>
        //Redirect(routes.ApplicationController.index).flashing("error" -> s"Error: $errors"),
        Redirect(routes.ProfileController.edit).flashing("error" -> s"Error: ${errors.toString}"),
      profileSuccess => {
        val profile = profileSuccess.copy(userID = request.identity.userID)
        if (profile.subjectImprove1 == profile.subjectImprove2 ||
          profile.subjectGoodAt1 == profile.subjectGoodAt2 ||
          List(profile.interest1, profile.interest2, profile.interest3).distinct.size < 3)
          Redirect(routes.ProfileController.edit()).flashing("error" -> messages("profile.error.duplicate.subjects.or.interests"))
        else {
          userService.saveProfile(profile)
          //Redirect(routes.ProfileController.view())
          Redirect(routes.QuestionsController.index)
        }
      }
    ))
  }

  private def psychogramDataJsonString(profile: TuteeProfile, psychoSubcategoryResult: Seq[(String, String, Double)], messages: Messages): String = {
    import PsychogramData._
    import utils.ScoreUtils._
    // TODO: refactor this whole method
    val subjects = "subjects" -> PsychogramCategoryData(List(
      PsychogramSubcategoryData(messages("subject." + profile.subjectGoodAt1), reverseScore(profile.scoreSubjectGoodAt1), messages("subject." + profile.subjectGoodAt1)),
      PsychogramSubcategoryData(messages("subject." + profile.subjectGoodAt2), reverseScore(profile.scoreSubjectGoodAt2), messages("subject." + profile.subjectGoodAt2)),
      PsychogramSubcategoryData(messages("subject." + profile.subjectImprove1), reverseScore(profile.scoreSubjectImprove1), messages("subject." + profile.subjectImprove1)),
      PsychogramSubcategoryData(messages("subject." + profile.subjectImprove2), reverseScore(profile.scoreSubjectImprove2), messages("subject." + profile.subjectImprove2))
    ).sortBy(_.name)(Ordering[String].reverse), messages("subjects.description"))

    val interests = "interests" -> PsychogramCategoryData(List(
      PsychogramSubcategoryData(messages("interest." + profile.interest1), profile.timeInterest1, messages("interest." + profile.interest1)),
      PsychogramSubcategoryData(messages("interest." + profile.interest2), profile.timeInterest2, messages("interest." + profile.interest2)),
      PsychogramSubcategoryData(messages("interest." + profile.interest3), profile.timeInterest3, messages("interest." + profile.interest3))
    ).sortBy(_.name), messages("interests.description"))

    def psychoDataForSubcategory(subcategory: String) = {
      PsychogramCategoryData(psychoSubcategoryResult.groupBy(_._1).getOrElse(subcategory.capitalize, Seq()).map(g => PsychogramSubcategoryData(messages(subcategory + "." + g._2), percentageToScore(g._3), messages(subcategory + "." + g._2 + ".description"))).toList.sortBy(_.name), messages(subcategory + ".description"))
    }

    val environment = "environment" -> psychoDataForSubcategory("environment")
    val constitution = "constitution" -> psychoDataForSubcategory("constitution")
    val working = "working" -> psychoDataForSubcategory("working")

    Json.stringify(Json.toJson(Map(interests, environment, constitution, subjects, working)))

  }

}
