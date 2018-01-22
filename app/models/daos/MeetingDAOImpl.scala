package models
package daos

import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

import models.daos.MeetingDAOImpl._
import models.enums.UserType
import models.tables.MeetingTable
import play.api.db.slick.DatabaseConfigProvider
import slick.basic.DatabaseConfig
import slick.jdbc.{ JdbcBackend, JdbcProfile }
import slick.lifted.TableQuery

class MeetingDAOImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends MeetingDAO {

  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db

  import dbConfig.profile.api._

  override def find(id: Long) = db.run(meetingTable.filter(_.id === id).result.headOption)

  override def myMeetings(user: User, start: LocalDateTime, end: LocalDateTime) = {

    val query = meetingTable
      .filter(m => (if (user.userType == UserType.Tutee) m.tuteeID.nonEmpty && m.tuteeID.getOrElse(UUID.randomUUID()) === user.userID else m.tutorID === user.userID) && m.start <= end && m.end >= start)
    db.run(query.result)
  }

  override def save(meeting: Meeting) = {
    db.run((meetingTable returning meetingTable.map(_.id)).insertOrUpdate(meeting))
  }

  override def delete(id: Long) = {
    db.run(meetingTable.filter(_.id === id).delete)
  }

  override def tutorMeetings(tutorId: UUID, start: LocalDateTime, end: LocalDateTime) = {
    val query = meetingTable
      .filter(m => m.tutorID === tutorId && m.start <= end && m.end >= start)
    db.run(query.result)
  }
}

object MeetingDAOImpl {

  private val meetingTable = TableQuery[MeetingTable]
}