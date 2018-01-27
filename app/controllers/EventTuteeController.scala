package controllers

import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import models.Event
import models.enums.EventType
import models.services.EventService
import org.webjars.play.WebJarsUtil
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc.{ AbstractController, ControllerComponents }
import utils.auth.DefaultEnv

import scala.concurrent.{ ExecutionContext, Future }

class EventTuteeController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv],
  eventService: EventService
)(
  implicit
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder,
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def events(start: LocalDateTime, end: LocalDateTime) = silhouette.SecuredAction.async { implicit request =>
    eventService.myEvents(request.identity, start, end).map(a => Ok(Json.toJson(a.map {
      case m =>
        if (m.eventType == EventType.Confirmed) m.toJsEvent.copy(color = Some("green"))
        else if (m.eventType == EventType.Finished) m.toJsEvent.copy(color = Some("gray"))
        else m.toJsEvent
    })))
  }

  def tutorEvents(tutorID: UUID, start: LocalDateTime, end: LocalDateTime) = silhouette.SecuredAction.async { implicit request =>
    eventService.tutorEvents(request.identity.userID, tutorID, start, end).map(a => Ok(Json.toJson(a.map {
      case m if m.eventType == EventType.Availability =>
        m.toBackgroundJsEvent.copy(id = "available")
      case m if !m.tuteeID.contains(request.identity.userID) =>
        m.toBackgroundJsEvent.copy(color = Some("#ff9f89"))
      case m if m.tutorID != tutorID =>
        m.toJsEvent.copy(color = Some("gray"))
      case m =>
        if (m.eventType == EventType.Requested) m.toEditableJsEvent.copy(constraint = Some("available"))
        else if (m.eventType == EventType.Confirmed) m.toJsEvent.copy(color = Some("green"))
        else if (m.eventType == EventType.Finished) m.toJsEvent.copy(color = Some("gray"))
        else m.toJsEvent
    })))
  }

  def myLessons = silhouette.SecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.tuteeMyLessons(request.identity)))
  }

  def requestLesson(tutorId: UUID, start: LocalDateTime, end: LocalDateTime, description: String) = silhouette.SecuredAction.async { implicit request =>
    val event = Event(None, "Requested", start, end, Some(request.identity.userID), tutorId, EventType.Requested, Some(description))
    eventService.save(event).map(id => Ok(id.get.toString))
  }

  def details(eventID: Long) = silhouette.SecuredAction.async { implicit request =>
    eventService.getTutorEventDetails(eventID).map(d => Ok(Json.toJson(d)))
  }
}
