package models.services

import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

import models.daos.MeetingDAO
import models.{ Meeting, User }

class MeetingServiceImpl @Inject() (meetingDAO: MeetingDAO) extends MeetingService {

  override def find(id: Long) = meetingDAO.find(id)

  override def myMeetings(user: User, start: LocalDateTime, end: LocalDateTime) = meetingDAO.myMeetings(user, start, end)

  override def save(meeting: Meeting) = meetingDAO.save(meeting)

  override def delete(id: Long) = meetingDAO.delete(id)

  override def tutorMeetings(tutorId: UUID, start: LocalDateTime, end: LocalDateTime) = meetingDAO.tutorMeetings(tutorId, start, end)

}