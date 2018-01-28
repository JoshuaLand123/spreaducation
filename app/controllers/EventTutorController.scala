package controllers

import java.time.LocalDateTime
import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import models.Event
import models.enums.EventType
import models.services.{ EventService, UserService }
import org.webjars.play.WebJarsUtil
import play.api.i18n.{ I18nSupport, Messages }
import play.api.libs.json.Json
import play.api.libs.mailer.{ Email, MailerClient }
import play.api.mvc.{ AbstractController, ControllerComponents }
import utils.auth.DefaultEnv

import scala.concurrent.ExecutionContext

class EventTutorController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv],
  eventService: EventService,
  userService: UserService,
  mailerClient: MailerClient
)(
  implicit
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder,
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def events(start: LocalDateTime, end: LocalDateTime) = silhouette.SecuredAction.async { implicit request =>
    eventService.myEvents(request.identity, start, end).map(events => Ok(Json.toJson(events.map {
      case m if m.eventType == EventType.Availability =>
        if (thereAreLessonsWithinAvailabilityEvent(m, events)) m.toBackgroundJsEvent
        else m.toEditableJsEvent.copy(color = Some("#7dd322"))
      case m =>
        if (m.eventType == EventType.Requested) m.toJsEvent
        else if (m.eventType == EventType.Confirmed) m.toJsEvent.copy(color = Some("green"))
        else if (m.eventType == EventType.Finished) m.toJsEvent.copy(color = Some("gray"))
        else m.toJsEvent
    })))
  }

  def thereAreLessonsWithinAvailabilityEvent(m: Event, events: Seq[Event]): Boolean =
    m.eventType == EventType.Availability &&
      events.exists(event => event.eventType != EventType.Availability
        && event.end.isAfter(m.start) && event.start.isBefore(m.end))

  def myLessons = silhouette.SecuredAction.async { implicit request =>
    userService.retrieveTutorProfile(request.identity.userID).map {
      case Some(p) => Ok(views.html.tutorMyLessons(request.identity, p))
      case None    => Redirect(routes.TutorProfileController.edit)
    }
  }

  def addAvailability(start: LocalDateTime, end: LocalDateTime) = silhouette.SecuredAction.async { implicit request =>
    val event = Event(None, "Available", start, end, None, request.identity.userID, EventType.Availability)
    eventService.save(event).map(id => Ok(id.get.toString))
  }

  def details(eventID: Long) = silhouette.SecuredAction.async { implicit request =>
    eventService.getTuteeEventDetails(eventID).map(d => Ok(Json.toJson(d)))
  }

  def confirm(eventID: Long) = silhouette.SecuredAction.async { implicit request =>
    eventService.find(eventID).map(_.map { event =>
      userService.retrieve(event.tuteeID.get).map(_.map { tutee =>
        mailerClient.send(Email(
          subject = "Lesson confirmed",
          from = Messages("email.from"),
          to = Seq(tutee.email.get),
          bodyText = Some("Your lesson request was confirmed"),
          bodyHtml = Some("<p>Your lesson request was confirmed</p>")
        ))
      })
      event.copy(title = "Confirmed", eventType = EventType.Confirmed)
    }.map(eventService.save)).map(_ => Ok(""))
  }

  def decline(eventID: Long, reason: String) = silhouette.SecuredAction.async { implicit request =>
    eventService.find(eventID).map(_.map { event =>
      userService.retrieve(event.tuteeID.get).map(_.map { tutee =>
        mailerClient.send(Email(
          subject = "Lesson declined",
          from = Messages("email.from"),
          to = Seq(tutee.email.get),
          bodyText = Some(s"Your lesson request was declined. Reason: $reason"),
          bodyHtml = Some(s"<p>Your lesson request was declined. Reason: $reason</p>")
        ))
      })
      event.copy(title = "Declined", eventType = EventType.Declined, declineReason = Some(reason))
    }.map(eventService.save)).map(_ => Ok(""))
  }
}
