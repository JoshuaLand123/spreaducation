package models.daos

import java.time.LocalDateTime
import java.util.UUID

import models.{ Event, TuteeEventDetails, TutorEventDetails, User }

import scala.concurrent.Future

trait EventDAO {

  def find(id: Long): Future[Option[Event]]

  def myEvents(user: User, start: LocalDateTime, end: LocalDateTime): Future[Seq[Event]]

  def save(event: Event): Future[Option[Long]]

  def delete(id: Long): Future[Int]

  def tutorEvents(tuteeID: UUID, tutorId: UUID, start: LocalDateTime, end: LocalDateTime): Future[Seq[Event]]

  def getTuteeEventDetails(eventID: Long): Future[TuteeEventDetails]

  def getTutorEventDetails(eventID: Long): Future[TutorEventDetails]

}
