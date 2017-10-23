package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import forms.QuestionsForm
import forms.QuestionsForm._
import models.services.{ AnswerService, QuestionService, UserService }
import models.{ Answer, Question, QuestionWithAnswer }
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

  def view = silhouette.SecuredAction.async { implicit request =>

    questionService.retrieve(request.identity).map(_.map {
      case (q: Question, a: Option[Answer]) => QuestionWithAnswer(q.id, a.flatMap(_.id), q.questionText, a.map(_.score).getOrElse(3))
    }).map(q => Ok(views.html.questions(QuestionsForm.form, q, request.identity)))
  }

  def submit = silhouette.SecuredAction.async { implicit request =>
    Future.successful(form.bindFromRequest.fold(
      errors =>
        Ok(views.html.home(request.identity)),
      questionnaire => {
        val answers = questionnaire.questions.map(r => Answer(id = r.answerId, userId = request.identity.userID, questionId = r.questionId, score = r.value))
        answerService.saveAll(answers)
        Ok(views.html.home(request.identity))
      }
    ))
  }

}
