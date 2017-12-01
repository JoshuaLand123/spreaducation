package models.enums

object LessonType extends Enumeration {
  type LessonType = Value
  val Online = Value("Online")
  val Offline = Value("Offline")
  val Both = Value("Both")
}
