package models.enums

object EventType extends Enumeration {
  type EventType = Value
  val Available, Requested, Confirmed, Declined, Canceled, Finished = Value
}
