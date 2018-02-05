package controllers

import java.time.LocalDateTime
import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import models.enums.EventType
import models.services.{EventService, UserService}
import models.{Event, JsEvent}
import org.webjars.play.WebJarsUtil
import play.api.i18n.{I18nSupport, Messages}
import play.api.libs.json.Json
import play.api.libs.mailer.{Email, MailerClient}
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.auth.DefaultEnv

import scala.concurrent.ExecutionContext

class EventTutorController @Inject()(
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
) extends AbstractController(components)
    with I18nSupport {

  def events(start: LocalDateTime, end: LocalDateTime) = silhouette.SecuredAction.async { implicit request =>
    eventService
      .myEvents(request.identity, start, end)
      .map(events =>
        Ok(Json.toJson(events.map(event => {
          val messages = request.messages
          val jsEvent = event.toJsEvent(messages)
          event match {
            case m if m.eventType == EventType.Available =>
              if (thereAreLessonsWithinAvailableEvent(m, events)) jsEvent.asBackGround
              else jsEvent.asEditable.color("#7dd322")
            case m =>
              if (m.eventType == EventType.Requested) jsEvent
              else if (m.eventType == EventType.Confirmed) jsEvent.color("green")
              else if (m.eventType == EventType.Finished) jsEvent.color("gray")
              else jsEvent
          }
        }))))
  }

  def thereAreLessonsWithinAvailableEvent(m: Event, events: Seq[Event]): Boolean =
    m.eventType == EventType.Available &&
      events.exists(
        event =>
          event.eventType != EventType.Available
            && event.end.isAfter(m.start) && event.start.isBefore(m.end))

  def myLessons = silhouette.SecuredAction.async { implicit request =>
    userService.retrieveTutorProfile(request.identity.userID).map {
      case Some(p) => Ok(views.html.tutorMyLessons(request.identity, p))
      case None    => Redirect(routes.TutorProfileController.edit)
    }
  }

  def addAvailable(start: LocalDateTime, end: LocalDateTime) = silhouette.SecuredAction.async { implicit request =>
    val event = Event(None, start, end, None, request.identity.userID, EventType.Available)
    eventService
      .save(event)
      .map(id => Ok(Json.toJson(JsEvent(id.get.toString, Messages(EventType.Available.toString), start, end))))
  }

  def details(eventID: Long) = silhouette.SecuredAction.async { implicit request =>
    eventService.getTuteeEventDetails(eventID).map(d => Ok(Json.toJson(d)))
  }

  def confirm(eventID: Long) = silhouette.SecuredAction.async { implicit request =>
    eventService
      .find(eventID)
      .map(_.map { event =>
        userService
          .retrieve(event.tuteeID.get)
          .map(_.map { tutee =>
            mailerClient.send(Email(
              subject = Messages("email.lesson.confirm.subject"),
              from = Messages("email.from"),
              to = Seq(tutee.email.get),
              bodyHtml =
                Some(views.html.emails.lessonConfirm(tutee, routes.EventTuteeController.myLessons.absoluteURL()).body)
            ))
          })
        event.copy(eventType = EventType.Confirmed)
      }.map(eventService.save))
      .map(_ => Ok(Messages(EventType.Confirmed.toString)))
  }

  def decline(eventID: Long, reason: String) = silhouette.SecuredAction.async { implicit request =>
    eventService
      .find(eventID)
      .map(_.map { event =>
        userService
          .retrieve(event.tuteeID.get)
          .map(_.map { tutee =>
            mailerClient.send(Email(
              Messages("email.lesson.decline.subject"),
              from = Messages("email.from"),
              to = Seq(tutee.email.get),
              bodyHtml =
                Some(views.html.emails.lessonDecline(tutee, routes.EventTuteeController.myLessons.absoluteURL()).body)
            ))
          })
        event.copy(eventType = EventType.Declined, declineReason = Some(reason))
      }.map(eventService.save))
      .map(_ => Ok(""))
  }
}
