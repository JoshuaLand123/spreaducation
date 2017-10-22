package models

object UserType extends Enumeration {
  type UserType = Value
  val Tutee = Value("Tutee")
  val Tutor = Value("Tutor")
}
