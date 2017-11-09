package models.enums

object Language extends Enumeration {
  type Language = Value
  val Arabic = Value("Arabic")
  val English = Value("English")
  val French = Value("French")
  val German = Value("German")
  val Italian = Value("Italian")
  val Russian = Value("Russian")
  val Turkish = Value("Turkish")
  val Other = Value("Other")

}
