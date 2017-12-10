package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import forms.ProfileForm
import models.UserProfile
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
      val form = profile.map(ProfileForm.form.fill).getOrElse(ProfileForm.form)
      Ok(views.html.profileEdit(form, request.identity))
    })
  }

  def submit = silhouette.SecuredAction.async { implicit request =>
    val messages = request.messages
    Future.successful(ProfileForm.form.bindFromRequest.fold(
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

  private def psychogramDataJsonString(profile: UserProfile, psychoSubcategoryResult: Seq[(String, String, Double)], messages: Messages): String = {
    import PsychogramData._
    // TODO: refactor this whole method
    val expertise = {
      def reverseScore(score: Int) = score match {
        case 1 => 5
        case 2 => 4
        case 3 => 3
        case 4 => 2
        case 5 => 1
        case 6 => 1
      }

      "subjects" -> PsychogramCategoryData(List(
        PsychogramSubcategoryData(messages("subject." + profile.subjectGoodAt1), reverseScore(profile.scoreSubjectGoodAt1), messages("subject." + profile.subjectGoodAt1)),
        PsychogramSubcategoryData(messages("subject." + profile.subjectGoodAt2), reverseScore(profile.scoreSubjectGoodAt2), messages("subject." + profile.subjectGoodAt2)),
        PsychogramSubcategoryData(messages("subject." + profile.subjectImprove1), reverseScore(profile.scoreSubjectImprove1), messages("subject." + profile.subjectImprove1)),
        PsychogramSubcategoryData(messages("subject." + profile.subjectImprove2), reverseScore(profile.scoreSubjectImprove2), messages("subject." + profile.subjectImprove2))
      ).sortBy(_.name)(Ordering[String].reverse), messages("subjects.description"))
    }

    def percentageToScore(percentage: Double) =
      if (percentage < 0.2) 1
      else if (percentage < 0.4) 2
      else if (percentage < 0.6) 3
      else if (percentage < 0.8) 4
      else 5

    val interests = "interests" -> PsychogramCategoryData(List(
      PsychogramSubcategoryData(messages("interest." + profile.interest1), profile.timeInterest1, messages("interest." + profile.interest1)),
      PsychogramSubcategoryData(messages("interest." + profile.interest2), profile.timeInterest2, messages("interest." + profile.interest2)),
      PsychogramSubcategoryData(messages("interest." + profile.interest3), profile.timeInterest3, messages("interest." + profile.interest3))
    ).sortBy(_.name), messages("interests.description"))

    val environment = "environment" -> PsychogramCategoryData(psychoSubcategoryResult.groupBy(_._1).getOrElse("Environment", Seq()).map(g => PsychogramSubcategoryData(messages("environment." + g._2), percentageToScore(g._3), messages("environment." + g._2 + ".description"))).toList.sortBy(_.name), messages("environment.description"))
    val constitution = "constitution" -> PsychogramCategoryData(psychoSubcategoryResult.groupBy(_._1).getOrElse("Constitution", Seq()).map(g => PsychogramSubcategoryData(messages("constitution." + g._2), percentageToScore(g._3), messages("constitution." + g._2 + ".description"))).toList.sortBy(_.name), messages("constitution.description"))
    val working = "working" -> PsychogramCategoryData(psychoSubcategoryResult.groupBy(_._1).getOrElse("Working", Seq()).map(g => PsychogramSubcategoryData(messages("working." + g._2), percentageToScore(g._3), messages("working." + g._2 + ".description"))).toList.sortBy(_.name)(Ordering[String].reverse), messages("working.description"))

    Json.stringify(Json.toJson(Map(interests, environment, constitution, expertise, working)))
  }

}
