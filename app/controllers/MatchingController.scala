package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import org.webjars.play.WebJarsUtil
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.auth.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

class MatchingController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv]
)(
  implicit
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder,
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def view = silhouette.SecuredAction.async { implicit request =>

    Future.successful(Ok(views.html.matching(request.identity)))
  }

}
