package models.services

import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

import models.daos.EventDAO
import models.{ Event, User }

class EventServiceImpl @Inject() (eventDAO: EventDAO) extends EventService {

  override def find(id: Long) = eventDAO.find(id)

  override def myEvents(user: User, start: LocalDateTime, end: LocalDateTime) = eventDAO.myEvents(user, start, end)

  override def save(event: Event) = eventDAO.save(event)

  override def delete(id: Long) = eventDAO.delete(id)

  override def tutorEvents(tuteeID: UUID, tutorId: UUID, start: LocalDateTime, end: LocalDateTime) = eventDAO.tutorEvents(tuteeID, tutorId, start, end)

  override def getTuteeEventDetails(eventID: Long) = eventDAO.getTuteeEventDetails(eventID)

  override def getTutorEventDetails(eventID: Long) = eventDAO.getTutorEventDetails(eventID)

}