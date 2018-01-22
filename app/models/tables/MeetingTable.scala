package models
package tables

import java.time.LocalDateTime
import java.util.UUID

import models.enums.MeetingType.MeetingType
import slick.collection.heterogeneous._
import slick.collection.heterogeneous.syntax._
import slick.jdbc.PostgresProfile.api._

class MeetingTable(tag: Tag) extends Table[Meeting](tag, "meetings") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def start = column[LocalDateTime]("start_time")
  def end = column[LocalDateTime]("end_time")
  def tuteeID = column[Option[UUID]]("tutee_id")
  def tutorID = column[UUID]("tutor_id")
  def meetingType = column[MeetingType]("meeting_type")

  override def * = (id.? :: title :: start :: end :: tuteeID :: tutorID :: meetingType :: HNil) <> (tuple, unapply)

  type MeetingHList = Option[Long] :: String :: LocalDateTime :: LocalDateTime :: Option[UUID] :: UUID :: MeetingType :: HNil

  def tuple(data: MeetingHList): Meeting = data match {

    case id :: title :: start :: end :: tuteeID :: tutorID :: meetingType :: HNil =>
      Meeting(id, title, start, end, tuteeID, tutorID, meetingType)
  }

  def unapply(meeting: Meeting): Option[MeetingHList] = meeting match {

    case Meeting(id, title, start, end, tuteeID, tutorID, meetingType) =>
      Some(id :: title :: start :: end :: tuteeID :: tutorID :: meetingType :: HNil)
  }
}
