package models.enums

import play.api.i18n.Messages

object Interest extends Enumeration {
  val Aerobic, AlpinSki, Art, Badminton, Basketball, Beachvolleyball, Biking, Bodyforming, Canoeing, Capoeira, Climbing,
  CoastalRowing, Crossfit, Crossgolf, Dance, DiscGolf, Diving, Fashion, Foosball, Football, ForeignLanguages,
  Freeclimbing, Frisbee, Golf, Gymnastics, Handball, Hockey, HorseRiding, IceScating, IceSkating, InlineSkating,
  Jogging, Judo, Kickboard, Kitesurfing, MarathonRunning, MartialArts, MeetFriends, MountainTrekking, Mountainbike,
  Music, Mystery, NordicWalking, Orienteering, Photography, Pilates, Puzzle, Qigong, Reading, RollerSkating,
  Rollerblading, Rowing, Rugby, Running, Sailing, Skateboarding, Skating, Slackline, Snorkeling, Snowboard, Squash,
  Streetball, StrengthTraining, Surfing, Swimming, TableTennis, TaeBo, TaiChi, Tennis, Theater, UltimateFrisbee,
  VideoGames, Volleyball, Walking, WatchTV, WaterAerobics, Windsurfing, XGolf, Yoga, Zumba =
    Value

  def selectList(messages: Messages) =
    Interest.values.toSeq
      .map(g => g.toString -> messages("interest." + g.toString))
      .sortBy(_._2) :+ ("Other" -> messages("interest.Other"))

}
