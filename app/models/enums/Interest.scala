package models.enums

import play.api.i18n.Messages

object Interest extends Enumeration {
  type Interest = Value
  val Music = Value("Music")
  val Art = Value("Art")
  val VideoGames = Value("VideoGames")
  val MeetFriends = Value("MeetFriends")
  val Fashion = Value("Fashion")
  val Theater = Value("Theater")
  val Mystery = Value("Mystery")
  val Puzzle = Value("Puzzle")
  val Dance = Value("Dance")
  val ForeignLanguages = Value("ForeignLanguages")
  val Reading = Value("Reading")
  val WatchTV = Value("WatchTV")
  val Yoga = Value("Yoga")
  val Foosball = Value("Foosball")
  val Tennis = Value("Tennis")
  val TableTennis = Value("TableTennis")
  val Rugby = Value("Rugby")
  val Football = Value("Football")
  val StrengthTraining = Value("StrengthTraining")
  val Climbing = Value("Climbing")
  val Hockey = Value("Hockey")
  val SocialCommitment = Value("SocialCommitment")

  def selectList(messages: Messages) =
    Interest.values.toSeq.map(g => g.toString -> messages("interest." + g.toString)).sortBy(_._2) :+ ("Other" -> messages("interest.Other"))

}