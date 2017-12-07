package helpers

import models.enums.Interest
import models.{Tutor, UserProfile}
import play.api.i18n.Messages

import scala.util.Random

object TutorGenerator {

  private val predefinedTutors = List(
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
    ("Joana ", "Knoth", "Female")
  )

  private val predefinedPricesAndStatuses = List(
    (1, 95, 72.60, "Legende"),
    (2, 85, 60.50, "Profi"),
    (3, 75, 48.40, "Halb-Profi"),
    (4, 65, 36.30, "Fortgeschritten"),
    (5, 55, 24.20, "Friscahling"),
  )

  private def predefinedDescriptions(subject: String, interest: String, gender: String) = {
    val tutorString = if (gender == "Female") "Tutorin" else "Tutor"
    List(
      s"Ich bin begeisterte${if (gender == "Male") "r" else ""} $subject $tutorString und mache in meiner Freizeit gern $interest.",
      s"Ich studiere $subject auf Lehramt und interessiere mich für $interest.",
      s"Dir $subject beizubringen ist für mich kein Problem. Neben dem Unterrichten interessiert mich $interest.",
      s"$subject macht mir viel Spaß. Dir vielleicht bald auch.",
      s"Seit ein paar Jahren schon unterrichte ich $subject. Nebenher beschäftige ich mich mit $interest.",
      s"Ich freue mich dir $subject näher zu bringen. Interessierst du dich auch für $interest?",
      s"Ich gebe leidentschaftlich $subject-Unterricht.",
      s"Bald kann ich dir helfen deine $subject-Noten zu verbessern.",
      s"Als $subject $tutorString kann ich dich gerne unterstützen. Außerdem interessiere ich mich für $interest.",
      s"Gemeinsam können wir dein Lernen in $subject verbessern. Ich freue mich.",
    )
  }

  def generateTutors(userProfile: UserProfile, messages: Messages): List[Tutor] = {
    predefinedPricesAndStatuses.map { case (order, score, price, status) =>
      val index = userProfile.userID.hashCode() + order
      val tutor = predefinedTutors(index % predefinedTutors.size)
      val otherInterest = Interest.values.toList(index % Interest.values.size).toString
      val interest = messages(s"interest.${if (order <= 2) userProfile.interest1 else otherInterest}")
      val subject = messages(s"subject.${userProfile.subjectImprove1}")
      val gender = tutor._3
      val descriptionList = predefinedDescriptions(subject, interest, gender)
      val description = descriptionList(index % descriptionList.size)
      val avatar = if (gender == "Male") s"avatars/male-${index % 14 + 1}.jpeg" else s"avatars/female-${index % 9 + 1}.jpeg"
      Tutor(
        firstName = tutor._1,
        lastName = tutor._2,
        description = description,
        avatarUrl = avatar,
        price = price,
        subject = subject,
        interest = interest,
        matchingScore = score + Random.nextInt(3),
        status = status,
        order = order
      )
    }
  }

}