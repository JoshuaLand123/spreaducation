package models
package tables

import java.time.LocalDateTime
import java.util.UUID

import models.enums.EventType.EventType
import slick.collection.heterogeneous._
import slick.collection.heterogeneous.syntax._
import slick.jdbc.PostgresProfile.api._

class EventTable(tag: Tag) extends Table[Event](tag, "events") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def start = column[LocalDateTime]("start_time")
  def end = column[LocalDateTime]("end_time")
  def tuteeID = column[Option[UUID]]("tutee_id")
  def tutorID = column[UUID]("tutor_id")
  def eventType = column[EventType]("event_type")
  def description = column[Option[String]]("description")
  def declineReason = column[Option[String]]("decline_reason")

  override def * =
    (id.? :: start :: end :: tuteeID :: tutorID :: eventType :: description :: declineReason :: HNil) <> (tuple, unapply)

  type EventHList = Option[Long] :: LocalDateTime :: LocalDateTime :: Option[UUID] :: UUID :: EventType ::
    Option[String] :: Option[String] :: HNil

  def tuple(data: EventHList): Event = data match {

    case id :: start :: end :: tuteeID :: tutorID :: eventType :: description :: declineReason :: HNil =>
      Event(id, start, end, tuteeID, tutorID, eventType, description, declineReason)
  }

  def unapply(event: Event): Option[EventHList] = event match {

    case Event(id, start, end, tuteeID, tutorID, eventType, description, declineReason) =>
      Some(id :: start :: end :: tuteeID :: tutorID :: eventType :: description :: declineReason :: HNil)
  }
}
