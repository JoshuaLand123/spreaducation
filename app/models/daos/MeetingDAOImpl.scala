package models
package daos

import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

import models.daos.EventDAOImpl._
import models.enums.{ EventType, UserType }
import models.tables.EventTable
import play.api.db.slick.DatabaseConfigProvider
import slick.basic.DatabaseConfig
import slick.jdbc.{ GetResult, JdbcBackend, JdbcProfile }
import slick.lifted.TableQuery

class EventDAOImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends EventDAO {

  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import dbConfig.profile.api._

  override def find(id: Long) = db.run(eventTable.filter(_.id === id).result.headOption)

  override def myEvents(user: User, start: LocalDateTime, end: LocalDateTime) = {

    val query = eventTable
      .filter(m => (if (user.userType == UserType.Tutee) m.tuteeID.nonEmpty && m.tuteeID.getOrElse(UUID.randomUUID()) === user.userID else m.tutorID === user.userID)
        && m.eventType =!= EventType.Canceled
        && m.eventType =!= EventType.Declined
        && m.start <= end && m.end >= start)
    db.run(query.result)
  }

  override def save(event: Event) = {
    db.run((eventTable returning eventTable.map(_.id)).insertOrUpdate(event))
  }

  override def delete(id: Long) = {
    db.run(eventTable.filter(_.id === id).delete)
  }

  override def tutorEvents(tuteeID: UUID, tutorID: UUID, start: LocalDateTime, end: LocalDateTime) = {
    val query = eventTable
      .filter(m => (m.tutorID === tutorID || m.tuteeID.getOrElse(UUID.randomUUID()) === tuteeID)
        && m.eventType =!= EventType.Canceled
        && m.eventType =!= EventType.Declined
        && m.start <= end && m.end >= start)
    db.run(query.result)
  }

  override def getTuteeEventDetails(eventID: Long) = {
    implicit val getTuteeEventDetailsResult = GetResult(r => TuteeEventDetails(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
    val query = sql"""
                  select u.first_name, u.last_name, p.main_language, p.subject_improve_1, p.score_subject_improve_1, p.subject_improve_2, p.score_subject_improve_2, e.description, e.event_type
                  from users u
                  join tutee_profile p on u.user_id = p.user_id
                  join events e on e.tutee_id = u.user_id
                  where e.id = $eventID;
           """.as[TuteeEventDetails]

    db.run(query.head)
  }

  override def getTutorEventDetails(eventID: Long) = {
    implicit val getTutorEventDetailsResult = GetResult(r => TutorEventDetails(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
    val query = sql"""
                  select u.first_name, u.last_name, p.main_language, p.subject_1, p.subject_2, p.subject_3, p.subject_4, e.description, e.event_type
                  from users u
                  join tutor_profile p on u.user_id = p.user_id
                  join events e on e.tutor_id = u.user_id
                  where e.id = $eventID;
           """.as[TutorEventDetails]

    db.run(query.head)
  }

}

object EventDAOImpl {

  private val eventTable = TableQuery[EventTable]
}