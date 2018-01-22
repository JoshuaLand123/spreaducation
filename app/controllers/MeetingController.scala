package controllers

import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import models.Meeting
import models.enums.MeetingType
import models.services.MeetingService
import org.webjars.play.WebJarsUtil
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc.{ AbstractController, ControllerComponents }
import utils.auth.DefaultEnv

import scala.concurrent.ExecutionContext

class MeetingController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv],
  meetingService: MeetingService
)(
  implicit
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder,
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def events(start: LocalDateTime, end: LocalDateTime) = silhouette.SecuredAction.async { implicit request =>
    meetingService.myMeetings(request.identity, start, end).map(a => Ok(Json.toJson(a.map(_.toEvent))))
  }

  def addEvent(start: LocalDateTime, end: LocalDateTime) = silhouette.SecuredAction.async { implicit request =>
    val meeting = Meeting(None, "Available", start, end, None, request.identity.userID, MeetingType.Availability)
    meetingService.save(meeting).map(id => Ok(id.get.toString))
  }

  def requestEvent(tutorId: UUID, start: LocalDateTime, end: LocalDateTime) = silhouette.SecuredAction.async { implicit request =>
    val meeting = Meeting(None, "Request", start, end, Some(request.identity.userID), tutorId, MeetingType.Requested)
    meetingService.save(meeting).map(id => Ok(id.get.toString))
  }

  def delete(id: Long) = silhouette.SecuredAction.async { implicit request =>
    meetingService.delete(id).map {
      case count if count == 1 => Ok("Success")
      case _                   => InternalServerError("Something went wrong")
    }
  }

  def update(id: Long, start: LocalDateTime, end: LocalDateTime) = silhouette.SecuredAction.async { implicit request =>

    meetingService.find(id).map(_.map(m => m.copy(start = start, end = end)).map(meetingService.save))
      .map(_ => Ok("Success"))
  }

  def tutorMeetings(tutorId: UUID, start: LocalDateTime, end: LocalDateTime) = silhouette.SecuredAction.async { implicit request =>
    meetingService.tutorMeetings(tutorId, start, end).map(a => Ok(Json.toJson(a.map {
      case m if m.meetingType == MeetingType.Availability =>
        m.toEvent.copy(id = "available", rendering = Some("background"))
      case m if m.tuteeID.contains(request.identity.userID) => m.toEvent.copy(constraint = Some("available"));
      case m => m.toEvent.copy(rendering = Some("background"), color = Some("#ff9f89"))
    })))
  }
}
