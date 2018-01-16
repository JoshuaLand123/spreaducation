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
  val Photography = Value("Photography")
  val Aerobic = Value("Aerobic")
  val AlpinSki = Value("AlpinSki")
  val Aquagymnastik = Value("Aquagymnastik")
  val Ausdauersport = Value("Ausdauersport")
  val Badminton = Value("Badminton")
  val Basketball = Value("Basketball")
  val Beachvolleyball = Value("Beachvolleyball")
  val Bergtrekking = Value("Bergtrekking")
  val Bodyforming = Value("Bodyforming")
  val Capoeira = Value("Capoeira")
  val CoastalRowing = Value("CoastalRowing")
  val Crossfit = Value("Crossfit")
  val Crossgolf = Value("Crossgolf")
  val DiscGolf = Value("DiscGolf")
  val Eislaufen = Value("Eislaufen")
  val Fitnessgymnastik = Value("Fitnessgymnastik")
  val Freeclimbing = Value("Freeclimbing")
  val Frisbee = Value("Frisbee")
  val Gerateturnen = Value("Gerateturnen")
  val Golf = Value("Golf")
  val Gymnastik = Value("Gymnastik")
  val Handball = Value("Handball")
  val Hindernislauf = Value("Hindernislauf")
  val InlineSkating = Value("InlineSkating")
  val Jogging = Value("Jogging")
  val Judo = Value("Judo")
  val Kampfsportarten = Value("Kampfsportarten")
  val Kanufahren = Value("Kanufahren")
  val Kickboard = Value("Kickboard")
  val Kitesurfen = Value("Kitesurfen")
  val Langlauf = Value("Langlauf")
  val Laufen = Value("Laufen")
  val Longboardfahren = Value("Longboardfahren")
  val Marathonlaufen = Value("Marathonlaufen")
  val Mountainbike = Value("Mountainbike")
  val NordicWalking = Value("NordicWalking")
  val Orientierungslauf = Value("Orientierungslauf")
  val Pilates = Value("Pilates")
  val Qigong = Value("Qigong")
  val Radfahren = Value("Radfahren")
  val Reiten = Value("Reiten")
  val Rollerblading = Value("Rollerblading")
  val Rollschuhlaufen = Value("Rollschuhlaufen")
  val Rudern = Value("Rudern")
  val Schlittschuhlaufen = Value("Schlittschuhlaufen")
  val Schnorcheln = Value("Schnorcheln")
  val Schwimmen = Value("Schwimmen")
  val Segeln = Value("Segeln")
  val Skateboardfahren = Value("Skateboardfahren")
  val Skaten = Value("Skaten")
  val Skilanglauf = Value("Skilanglauf")
  val Slackline = Value("Slackline")
  val Snowboard = Value("Snowboard")
  val Squash = Value("Squash")
  val Streetball = Value("Streetball")
  val Surfen = Value("Surfen")
  val TaeBo = Value("TaeBo")
  val TaiChi = Value("TaiChi")
  val Tauchen = Value("Tauchen")
  val UltimateFrisbee = Value("UltimateFrisbee")
  val Volleyball = Value("Volleyball")
  val Walking = Value("Walking")
  val Wassergymnastik = Value("Wassergymnastik")
  val Windsurfen = Value("Windsurfen")
  val Wirbelsaulengymnastik = Value("Wirbelsaulengymnastik")
  val XGolf = Value("XGolf")
  val Zumba = Value("Zumba")

  def selectList(messages: Messages) =
    Interest.values.toSeq.map(g => g.toString -> messages("interest." + g.toString)).sortBy(_._2) :+ ("Other" -> messages("interest.Other"))

}