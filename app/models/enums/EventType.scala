package models.enums

object EventType extends Enumeration {
  type EventType = Value
  val Availability = Value("Availability")
  val Requested = Value("Requested")
  val Confirmed = Value("Confirmed")
  val Declined = Value("Declined")
  val Canceled = Value("Canceled")
  val Finished = Value("Finished")
}
