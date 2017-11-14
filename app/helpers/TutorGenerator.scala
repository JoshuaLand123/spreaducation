package helpers

import models.enums.Interest
import models.{Tutor, UserProfile}

import scala.util.Random

object TutorGenerator {
  private def randomTutor = {
    val predefinedTutors = List(
      ("Bugs", "Bunny", "avatar.jpeg", "Male"),
      ("Homer", "Simpson", "avatar.jpeg", "Male"),
      ("Mickey", "Mouse", "avatar.jpeg", "Male"),
      ("Bart", "Simpson", "avatar.jpeg", "Male"),
      ("Charlie", "Brown", "avatar.jpeg", "Male"),
      ("Fred", "Flintstone", "avatar.jpeg", "Male"),
      ("Wile", "E. Coyote", "avatar.jpeg", "Male"),
      ("Eric", "Cartman", "avatar.jpeg", "Male"),
      ("Daffy", "Duck", "avatar.jpeg", "Male"),
      ("Porky", "Pig", "avatar.jpeg", "Male"),
      ("Scooby", "Doo", "avatar.jpeg", "Male"),
      ("Porky", "Pig", "avatar.jpeg", "Male"),
      ("Pink", "Panther", "avatar.jpeg", "Male"),
      ("Winnie", "the Pooh", "avatar.jpeg", "Male"),
      ("Donald", "Duck", "avatar.jpeg", "Male"),
      ("Woody", "Woodpecker", "avatar.jpeg", "Male"),
      ("Spider", "Man", "avatar.jpeg", "Male"),
      ("Turanga", "Leela", "avatar.jpeg", "Female"),
      ("Princess", "Tiana", "avatar.jpeg", "Female"),
      ("Lois", "Griffin", "avatar.jpeg", "Female"),
      ("Lisa", "Simpson", "avatar.jpeg", "Female"),
      ("Usagi", "Tsukino", "avatar.jpeg", "Female"),
      ("Eliza", "Thornberry", "avatar.jpeg", "Female"))
    predefinedTutors(Random.nextInt(predefinedTutors.size))
  }
  private def randomPrice = {
    val predefinedPrices = List(24.20, 36.30, 48.40, 56.50)
    predefinedPrices(Random.nextInt(predefinedPrices.size))
  }

  private def randomDescription(subject: String, allInterests: List[String], gender: String) = {
    val interests = allInterests.distinct.mkString(",")
    val tutorString = if (gender == "Female") "Tutorin" else "Tutor"
    val predefinedDescriptions = List(
      s"Ich bin begeisterter $subject $tutorString und mache in meiner Freizeit gern $interests.",
      s"Ich studiere $subject auf Lehramt und interessiere mich für $interests.",
      s"Dir $subject beizubringen ist für mich kein Problem. Neben dem Unterrichten interessiert mich $interests.",
      s"$subject macht mir viel Spaß. Dir vielleicht bald auch.",
      s"Seit ein paar Jahren schon unterrichte ich $subject. Nebenher beschäftige ich mich mit $interests.",
      s"Ich freue mich dir $subject näher zu bringen. Interessierst du dich auch für $interests?",
      s"Ich gebe leidentschaftlich $subject-Unterricht.",
      s"Bald kann ich dir helfen deine $subject-Noten zu verbessern.",
      s"Als $subject $tutorString kann ich dich gerne unterstützen. Außerdem interessiere ich mich für $interests.",
      s"Gemeinsam können wir dein Lernen in $subject verbessern. Ich freue mich.",
    )

      predefinedDescriptions(Random.nextInt(predefinedDescriptions.size))
  }

  private def randomMatchingScore(order: Int): Int = {
    val matchingScoreList = List(95, 85, 75, 65, 55, 45)
    matchingScoreList(order) + Random.nextInt(3)
  }

  def generateTutors(userProfile: UserProfile): List[Tutor] = {
    (0 to Random.nextInt(5) + 1 ).toList.map { n =>
      val tutor = randomTutor
      val randomInterest = Interest.values.toList(Random.nextInt(Interest.values.size)).toString
      val interest = if (n <= 1) userProfile.interest1 else randomInterest
      val avatar = if (tutor._4 == "Male") s"avatars/male-${Random.nextInt(8) + 1 + n}.jpeg" else s"avatars/female-${Random.nextInt(3) + 1 + n}.jpeg"
      Tutor(
        firstName = tutor._1,
        lastName = tutor._2,
        description = randomDescription(userProfile.subjectImprove1, List(interest), tutor._4),
        avatarUrl = avatar,
        price = randomPrice,
        subject = userProfile.subjectImprove1,
        interest = interest,
        matchingScore = randomMatchingScore(n)
      )
    }
  }

}