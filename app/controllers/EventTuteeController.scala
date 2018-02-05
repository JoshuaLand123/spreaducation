package controllers

import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import models.{Event, JsEvent}
import models.enums.EventType
import models.services.{EventService, UserService}
import org.webjars.play.WebJarsUtil
import play.api.i18n.{I18nSupport, Messages}
import play.api.libs.json.Json
import play.api.libs.mailer.{Email, MailerClient}
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.auth.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

class EventTuteeController @Inject()(
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

  def tutorCalendar(tutorID: UUID) = silhouette.SecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.tuteeRequestLessons(request.identity, tutorID.toString)))
  }

  def events(start: LocalDateTime, end: LocalDateTime) = silhouette.SecuredAction.async { implicit request =>
    eventService
      .myEvents(request.identity, start, end)
      .map(a =>
        Ok(Json.toJson(a.map { m =>
          val messages = request.messages
          val jsEvent = m.toJsEvent(messages)
          if (m.eventType == EventType.Confirmed) jsEvent.copy(color = Some("green"))
          else if (m.eventType == EventType.Finished) jsEvent.copy(color = Some("gray"))
          else jsEvent
        })))
  }

  def tutorEvents(tutorID: UUID, start: LocalDateTime, end: LocalDateTime) = silhouette.SecuredAction.async {
    implicit request =>
      eventService
        .tutorEvents(request.identity.userID, tutorID, start, end)
        .map(a =>
          Ok(Json.toJson(a.map(event => {
            val messages = request.messages
            val jsEvent = event.toJsEvent(messages)
            event match {
              case m if m.eventType == EventType.Available =>
                jsEvent.asBackGround.copy(id = "available")
              case m if !m.tuteeID.contains(request.identity.userID) =>
                jsEvent.asBackGround.color("#ff9f89")
              case m if m.tutorID != tutorID =>
                jsEvent.color("gray")
              case m =>
                if (m.eventType == EventType.Requested) jsEvent.asEditable.copy(constraint = Some("available"))
                else if (m.eventType == EventType.Confirmed) jsEvent.color("green")
                else if (m.eventType == EventType.Finished) jsEvent.color("gray")
                else jsEvent
            }
          }))))
  }

  def myLessons = silhouette.SecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.tuteeMyLessons(request.identity)))
  }

  def requestLesson(tutorId: UUID, start: LocalDateTime, end: LocalDateTime, description: String) =
    silhouette.SecuredAction.async { implicit request =>
      val event =
        Event(None, start, end, Some(request.identity.userID), tutorId, EventType.Requested, Some(description))
      userService
        .retrieve(tutorId)
        .map(_.map { tutor =>
          mailerClient.send(Email(
            subject = Messages("email.lesson.request.subject"),
            from = Messages("email.from"),
            to = Seq(tutor.email.get),
            bodyHtml =
              Some(views.html.emails.lessonRequest(tutor, routes.EventTutorController.myLessons.absoluteURL()).body)
          ))
        })
      val messages = request.messages
      eventService
        .save(event)
        .map(id =>
          Ok(
            Json.toJson(JsEvent(id.map(_.toString).getOrElse(""), messages(EventType.Requested.toString), start, end))))
    }

  def details(eventID: Long) = silhouette.SecuredAction.async { implicit request =>
    eventService.getTutorEventDetails(eventID).map(d => Ok(Json.toJson(d)))
  }
}
