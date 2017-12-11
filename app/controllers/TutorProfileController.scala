package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import models.services.{ QuestionService, UserService }
import org.webjars.play.WebJarsUtil
import play.api.i18n._
import play.api.mvc.{ AbstractController, ControllerComponents }
import utils.auth.DefaultEnv

import scala.concurrent.ExecutionContext

class TutorProfileController @Inject() (
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

}
