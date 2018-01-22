package models.services

import java.time.LocalDateTime
import java.util.UUID

import models.{ Meeting, User }

import scala.concurrent.Future

trait MeetingService {

  def find(id: Long): Future[Option[Meeting]]

  def myMeetings(user: User, start: LocalDateTime, end: LocalDateTime): Future[Seq[Meeting]]

  def save(meeting: Meeting): Future[Option[Long]]

  def delete(id: Long): Future[Int]

  def tutorMeetings(tutorId: UUID, start: LocalDateTime, end: LocalDateTime): Future[Seq[Meeting]]

}
