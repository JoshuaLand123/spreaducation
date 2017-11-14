package helpers

import models.enums.Interest
import models.{Tutor, UserProfile}
import play.api.i18n.Messages

import scala.util.Random

object TutorGenerator {
  private def randomTutor = {
    val predefinedTutors = List(
      ("Juliane", "Jüttner", "Female"),
      ("Miriam", "Klein", "Female"),
      ("Nora", "Meier", "Female"),
      ("Aaron", "Dorsch", "Male"),
      ("Edith", "Lamm", "Female"),
      ("Rebecca", "Prieß", "Female"),
      ("Noah", "Buschmann", "Male"),
      ("Barbara", "Lehr", "Female"),
      ("Birte", "Frosch", "Female"),
      ("Linus", "Jarre", "Male"),
      ("Daniela", "Marks", "Female"),
      ("Benedikt", "Graefe", "Male"),
      ("Claus", "Tittel", "Male"),
      ("Erik", "Bargfrede", "Male"),
      ("Laurin", "Schelle", "Female"),
      ("Jennifer", "Fenten", "Female"),
      ("Hella", "Jost", "Female"),
      ("Waltraud", "Hase", "Male"),
      ("Luisa", "Gmelin", "Female"),
      ("Henrik", "Lochner", "Male"),
      ("Katja", "Wütschner", "Female"),
      ("Jürgen", "Vennemann", "Male"),
      ("Rita", "Jansen", "Female"),
      ("Marie", "Rhein", "Female"),
      ("Barbara", "Bedner", "Female"),
      ("Victoria", "Bülskämper", "Female"),
      ("Sigrid", "Langhorst", "Female"),
      ("Bärbel", "Müssemeier", "Female"),
      ("Yvonne", "Seib", "Female"),
      ("Stephanie", "Oßwald", "Female"),
      ("Lisa", "Langenberg", "Female"),
      ("Christa ", "Riegler", "Female"),
      ("Regine ", "Groh", "Female"),
      ("Thorben ", "Günther", "Male"),
      ("Wilfried ", "Riehl", "Male"),
      ("Robert ", "Thurner", "Male"),
      ("Dieter ", "Swoboda", "Male"),
      ("Phil ", "Seidel", "Male"),
      ("Lasse", "Wildermuth", "Male"),
      ("Jeremy ", "Brugger", "Male"),
      ("Burkhard ", "Holthaus", "Male"),
      ("Frank ", "Hochholzer", "Male"),
      ("Noah ", "Rothert", "Male"),
      ("Maximilian ", "Hünniger", "Male"),
      ("Erwin ", "Baar", "Male"),
      ("Vanessa ", "Rothmund", "Female"),
      ("Lidia ", "Melchior", "Female"),
      ("Jürgen ", "Frosch", "Male"),
      ("Irene ", "Kantel", "Female"),
      ("Joana ", "Knoth", "Female"))
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

  def generateTutors(userProfile: UserProfile, messages: Messages): List[Tutor] = {
    (0 to Random.nextInt(5) + 1 ).toList.map { n =>
      val tutor = randomTutor
      val randomInterest = Interest.values.toList(Random.nextInt(Interest.values.size)).toString
      val interest = messages(s"interest.${if (n <= 1) userProfile.interest1 else randomInterest}")
      val subject = messages(s"subject.${userProfile.subjectImprove1}")
      val gender = tutor._3
      val avatar = if (gender == "Male") s"avatars/male-${Random.nextInt(8) + 1 + n}.jpeg" else s"avatars/female-${Random.nextInt(3) + 1 + n}.jpeg"
      Tutor(
        firstName = tutor._1,
        lastName = tutor._2,
        description = randomDescription(subject, List(interest), gender),
        avatarUrl = avatar,
        price = randomPrice,
        subject = subject,
        interest = interest,
        matchingScore = randomMatchingScore(n)
      )
    }
  }

}