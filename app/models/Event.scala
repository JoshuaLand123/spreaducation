package models

import java.time.LocalDateTime
import java.util.UUID

import models.enums.EventType.EventType
import play.api.i18n.Messages
import play.api.libs.json._

case class Event(
    id: Option[Long],
    start: LocalDateTime,
    end: LocalDateTime,
    tuteeID: Option[UUID],
    tutorID: UUID,
    eventType: EventType,
    description: Option[String] = None,
    declineReason: Option[String] = None
) {
  def toJsEvent(messages: Messages) = {
    val title = messages(eventType.toString)
    JsEvent(id.map(_.toString).getOrElse(""), title, start, end, Some(eventType.toString))
  }

}

case class JsEvent(
    id: String,
    title: String,
    start: LocalDateTime,
    end: LocalDateTime,
    eventType: Option[String] = None,
    editable: Boolean = false,
    rendering: Option[String] = None,
    constraint: Option[String] = None,
    color: Option[String] = None,
    textColor: Option[String] = None,
    borderColor: Option[String] = None,
    backgroundColor: Option[String] = None,
    url: Option[String] = None,
    className: Option[String] = None //CSS Class name
) {
  def asEditable = this.copy(editable = true)
  def asBackGround = this.copy(rendering = Some("background"))
  def color(color: String) = this.copy(color = Some(color))
}

object JsEvent {
  implicit val eventFormat: Format[JsEvent] = Json.format[JsEvent]
}

case class TuteeEventDetails(
    firstName: Option[String],
    lastName: Option[String],
    //image: Option[Array[Byte]] = None,
    learningLanguage: String,
    subjectImprove1: String,
    scoreSubjectImprove1: Int,
    subjectImprove2: String,
    scoreSubjectImprove2: Int,
    eventDescription: Option[String] = None,
    eventType: String
)

object TuteeEventDetails {
  implicit val eventFormat: Format[TuteeEventDetails] = Json.format[TuteeEventDetails]
}

case class TutorEventDetails(
    firstName: Option[String],
    lastName: Option[String],
    //image: Option[Array[Byte]] = None,
    mainLanguage: String,
    subject1: String,
    subject2: Option[String],
    subject3: Option[String],
    subject4: Option[String],
    eventDescription: Option[String] = None,
    eventType: String
)

object TutorEventDetails {
  implicit val eventFormat: Format[TutorEventDetails] = Json.format[TutorEventDetails]
}
