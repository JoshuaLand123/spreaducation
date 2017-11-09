package models.enums

object Subject extends Enumeration {
  type Subject = Value
  val Math = Value("Math")
  val German = Value("German")
  val English = Value("English")
  val French = Value("French")
  val Spanish = Value("Spanish")
  val Latin = Value("Latin")
  val Italian = Value("Italian")
  val Chinese = Value("Chinese")
  val Politics = Value("Politics")
  val History = Value("History")
  val Art = Value("Art")
  val Music = Value("Music")
  val Physics = Value("Physics")
  val Chemistry = Value("Chemistry")
  val Geography = Value("Geography")
  val Other = Value("Other")
}