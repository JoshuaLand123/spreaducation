package models.enums

object DiscType extends Enumeration {
  type DiscType = Value
  val Dominant = Value("Dominant")
  val Influential = Value("Influential")
  val Steady = Value("Steady")
  val Compliant = Value("Compliant")
  val None = Value("None")
}
