package controllers

import java.nio.file.Files
import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import forms.TutorProfileForm
import models.TutorProfile
import models.services.{QuestionService, UserService}
import modules.{PsychogramCategoryData, PsychogramData, PsychogramSubcategoryData}
import org.webjars.play.WebJarsUtil
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.auth.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

class TutorProfileController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv],
  userService: UserService,
  questionService: QuestionService
)(
  implicit
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder,
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def view = silhouette.SecuredAction.async { implicit request =>

    userService.retrieveTutorProfile(request.identity.userID).flatMap {
      case Some(p) =>
        questionService.getPsychoSubcategoryScores(request.identity.userID).map(psychoResult =>
          Ok(views.html.profileTutor(request.identity, Some(p), psychogramDataJsonString(p, psychoResult, request.messages))))
      case None => Future.successful(Redirect(routes.TutorProfileController.edit))
    }
  }

  def edit = silhouette.SecuredAction.async { implicit request =>
    userService.retrieveTutorProfile(request.identity.userID).flatMap(profile => {
      val form = profile.map(TutorProfileForm.form.fill).getOrElse(TutorProfileForm.form)
      questionService.isAllowedToEditQuestions(request.identity).map(isAllowedToEditQuestions =>
        Ok(views.html.profileEditTutor(form, request.identity, isAllowedToEditQuestions)))
    })
  }

  def submit = silhouette.SecuredAction.async(parse.multipartFormData) { implicit request =>

    val messages = request.messages
    TutorProfileForm.form.bindFromRequest.fold(
      errors =>
        //Redirect(routes.ApplicationController.index).flashing("error" -> s"Error: $errors"),
        Future.successful(Redirect(routes.TutorProfileController.edit).flashing("error" -> messages("error"))),
      profileSuccess => {
        val bytes: Option[Array[Byte]] = request.body.file("picture").map(p => Files.readAllBytes(p.ref))
        val profile = profileSuccess.copy(userID = request.identity.userID)
        /*val subjects: List[String] = List(Some(profile.subjects.subject1), profile.subjects.subject2, profile.subjects.subject3, profile.subjects.subject4).flatten
        if (subjects.size != subjects.distinct.size ||
          List(profile.interests.interest1, profile.interests.interest2, profile.interests.interest3).distinct.size < 3)
          Future.successful(Redirect(routes.TutorProfileController.edit).flashing("error" -> messages("profile.error.duplicate.subjects.or.interests")))
        else {*/
          if (bytes.isDefined && bytes.get.nonEmpty) {
            userService.save(request.identity.copy(image = bytes))
          }
          userService.saveTutorProfile(profile).map(_ => Redirect(routes.QuestionsController.index))
        //}
      }
    )
  }

  def students = silhouette.SecuredAction.async { implicit request =>
    userService.retrieveTutorProfile(request.identity.userID).map {
      case Some(p) => Ok(views.html.tutorMyStudents(request.identity, p))
      case None => Redirect(routes.TutorProfileController.edit)
    }
  }

  private def psychogramDataJsonString(profile: TutorProfile, psychoSubcategoryResult: Seq[(String, String, Double)], messages: Messages): String = {
    import PsychogramData._
    import utils.ScoreUtils._
    // TODO: refactor this whole methodPsychogramCategoryData(List(
    val subjects = "subjects" -> PsychogramCategoryData(List(Some(PsychogramSubcategoryData(messages("subject." + profile.subjects.subject1), profile.subjects.subject1Level, messages("subject." + profile.subjects.subject1))),
      profile.subjects.subject2.map(s => PsychogramSubcategoryData(messages("subject." + s), profile.subjects.subject2Level.getOrElse(9) - 6, messages("subject." + s))),
      profile.subjects.subject3.map(s => PsychogramSubcategoryData(messages("subject." + s), profile.subjects.subject3Level.getOrElse(9) - 6, messages("subject." + s))),
      profile.subjects.subject4.map(s => PsychogramSubcategoryData(messages("subject." + s), profile.subjects.subject4Level.getOrElse(9) - 6, messages("subject." + s))),
    ).flatten.sortBy(_.name)(Ordering[String].reverse), messages("subjects.description.tutor"))

    val interests = "interests" -> PsychogramCategoryData(List(
      PsychogramSubcategoryData(messages("interest." + profile.interests.interest1), profile.interests.timeInterest1, messages("interest." + profile.interests.interest1)),
      PsychogramSubcategoryData(messages("interest." + profile.interests.interest2), profile.interests.timeInterest2, messages("interest." + profile.interests.interest2)),
      PsychogramSubcategoryData(messages("interest." + profile.interests.interest3), profile.interests.timeInterest3, messages("interest." + profile.interests.interest3))
    ).sortBy(_.name), messages("interests.description.tutor"))

    def psychoDataForSubcategory(subcategory: String) = {
      PsychogramCategoryData(psychoSubcategoryResult.groupBy(_._1).getOrElse(subcategory.capitalize, Seq()).map(g => PsychogramSubcategoryData(messages(subcategory + "." + g._2 + ".tutor"), percentageToScore(g._3), messages(subcategory + "." + g._2 + ".description.tutor"))).toList.sortBy(_.name), messages(subcategory + ".description.tutor"))
    }

    val environment = "environment" -> psychoDataForSubcategory("environment")
    val constitution = "constitution" -> psychoDataForSubcategory("constitution")
    val working = "working" -> psychoDataForSubcategory("working")

    Json.stringify(Json.toJson(Map(interests, environment, constitution, subjects, working)))

  }
}
