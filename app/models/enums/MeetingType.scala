package models.enums

object MeetingType extends Enumeration {
  type MeetingType = Value
  val Availability = Value("Availability")
  val Requested = Value("Requested")
  val Confirmed = Value("Confirmed")
  val Canceled = Value("Canceled")
  val Finished = Value("Finished")
}
