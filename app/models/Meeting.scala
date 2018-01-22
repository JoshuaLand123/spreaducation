package models

import java.time.LocalDateTime
import java.util.UUID

import models.enums.MeetingType.MeetingType
import play.api.libs.json._

case class Meeting(
  id: Option[Long],
  title: String,
  start: LocalDateTime,
  end: LocalDateTime,
  tuteeID: Option[UUID],
  tutorID: UUID,
  meetingType: MeetingType
) {
  def toEvent: Event = Event(id.map(_.toString).getOrElse(""), title, start, end)
}

case class Event(
  id: String,
  title: String,
  start: LocalDateTime,
  end: LocalDateTime,
  rendering: Option[String] = None,
  constraint: Option[String] = None,
  color: Option[String] = None,
  borderColor: Option[String] = None,
//allDay: Boolean
//end: Option[Date],
//allDay: Boolean,
//color: String,
//rendering: String,
//url:String,
//className: String //CSS Class name
//backgroundColor: String
//borderColor: String
//textColor: String
//constraint: Long  //Event id
)

object Event {
  implicit val eventFormat: Format[Event] = Json.format[Event]
}