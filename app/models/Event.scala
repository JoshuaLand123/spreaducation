package models

import java.time.LocalDateTime
import java.util.UUID

import models.enums.EventType.EventType
import play.api.libs.json._

case class Event(
  id: Option[Long],
  title: String,
  start: LocalDateTime,
  end: LocalDateTime,
  tuteeID: Option[UUID],
  tutorID: UUID,
  eventType: EventType,
  description: Option[String] = None,
  declineReason: Option[String] = None
) {
  def toJsEvent = JsEvent(id.map(_.toString).getOrElse(""), title, start, end)
  def toBackgroundJsEvent = JsEvent(id.map(_.toString).getOrElse(""), title, start, end, rendering = Some("background"))
  def toEditableJsEvent = JsEvent(id.map(_.toString).getOrElse(""), title, start, end, editable = true)
}

case class JsEvent(
  id: String,
  title: String,
  start: LocalDateTime,
  end: LocalDateTime,
  editable: Boolean = false,
  rendering: Option[String] = None,
  constraint: Option[String] = None,
  color: Option[String] = None,
  textColor: Option[String] = None,
  borderColor: Option[String] = None,
  backgroundColor: Option[String] = None,
  url: Option[String] = None,
  className: Option[String] = None //CSS Class name
)

object JsEvent {
  implicit val eventFormat: Format[JsEvent] = Json.format[JsEvent]
}

case class TuteeEventDetails(
  firstName: Option[String],
  lastName: Option[String],
  //image: Option[Array[Byte]] = None,
  mainLanguage: String,
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