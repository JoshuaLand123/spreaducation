package models.enums

object EventType extends Enumeration {
  type EventType = Value
  val Availability, Requested, Confirmed, Declined, Canceled, Finished = Value
}
