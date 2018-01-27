package controllers

import java.time.LocalDateTime
import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import models.services.{ EventService, UserService }
import org.webjars.play.WebJarsUtil
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, ControllerComponents }
import utils.auth.DefaultEnv

import scala.concurrent.ExecutionContext

class EventController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv],
  eventService: EventService
)(
  implicit
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder,
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def delete(eventID: Long) = silhouette.SecuredAction.async { implicit request =>
    eventService.delete(eventID).map {
      case count if count == 1 => Ok("Success")
      case _                   => InternalServerError("Something went wrong")
    }
  }

  def update(eventID: Long, start: LocalDateTime, end: LocalDateTime) = silhouette.SecuredAction.async { implicit request =>
    eventService.find(eventID).map(_.map(m => m.copy(start = start, end = end)).map(eventService.save))
      .map(_ => Ok("Success"))
  }

}
