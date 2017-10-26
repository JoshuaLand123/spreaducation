package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import forms.QuestionsForm
import forms.QuestionsForm._
import models.services.{ AnswerService, QuestionService }
import models.{ Answer, QuestionWithAnswer }
import org.webjars.play.WebJarsUtil
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, ControllerComponents }
import utils.auth.DefaultEnv

import scala.concurrent.{ ExecutionContext, Future }

class QuestionsController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv],
  questionService: QuestionService,
  answerService: AnswerService
)(
  implicit
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder,
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def index = view(1)

  def view(page: Int) = silhouette.SecuredAction.async { implicit request =>

    questionService.retrieve(request.identity, page, 5).map(model => (model.questionWithAnswers.map {
      case (q, a) => QuestionWithAnswer(q.id, a.flatMap(_.id), q.questionText, a.map(_.score).getOrElse(3))
    }, model.total)).map {
      case (q, total) =>
        val totalPages = Math.ceil(total / 5.0).toInt
        if (page > totalPages) Redirect(routes.ApplicationController.index).flashing("info" -> "Saved successfully")
        else Ok(views.html.questions(QuestionsForm.form, q, request.identity, page, totalPages))
    }
  }

  def submit = silhouette.SecuredAction.async { implicit request =>
    Future.successful(form.bindFromRequest.fold(
      errors =>
        Ok(views.html.home(request.identity)),
      questionnaire => {
        val answers = questionnaire.questions.map(r => Answer(id = r.answerId, userId = request.identity.userID, questionId = r.questionId, score = r.value))
        answerService.saveAll(answers)
        Redirect(routes.QuestionsController.view(questionnaire.page + 1))
      }
    ))
  }

}
