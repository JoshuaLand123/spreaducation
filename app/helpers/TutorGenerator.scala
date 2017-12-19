package helpers

import java.util.UUID

import models.enums.Interest
import models.{TuteeProfile, TutorMatch}
import play.api.i18n.Messages

object TutorGenerator {

  private val predefinedTutors = List(
    ("Juliane", "Jüttner", "Female"),
    ("Aaron", "Dorsch", "Male"),
    ("Miriam", "Klein", "Female"),
    ("Nora", "Meier", "Female"),
    ("Noah", "Buschmann", "Male"),
    ("Edith", "Lamm", "Female"),
    ("Rebecca", "Prieß", "Female"),
    ("Claus", "Tittel", "Male"),
    ("Barbara", "Lehr", "Female"),
    ("Birte", "Frosch", "Female"),
    ("Linus", "Jarre", "Male"),
    ("Daniela", "Marks", "Female"),
    ("Benedikt", "Graefe", "Male"),
    ("Erik", "Bargfrede", "Male"),
    ("Laurin", "Schelle", "Female"),
    ("Jeremy ", "Brugger", "Male"),
    ("Jennifer", "Fenten", "Female"),
    ("Hella", "Jost", "Female"),
    ("Waltraud", "Hase", "Male"),
    ("Luisa", "Gmelin", "Female"),
    ("Henrik", "Lochner", "Male"),
    ("Katja", "Wütschner", "Female"),
    ("Jürgen", "Vennemann", "Male"),
    ("Rita", "Jansen", "Female"),
    ("Marie", "Rhein", "Female"),
    ("Lasse", "Wildermuth", "Male"),
    ("Bärbel", "Müssemeier", "Female"),
    ("Yvonne", "Seib", "Female"),
    ("Stephanie", "Oßwald", "Female"),
    ("Thorben ", "Günther", "Male"),
    ("Lisa", "Langenberg", "Female"),
    ("Christa ", "Riegler", "Female"),
    ("Regine ", "Groh", "Female"),
    ("Wilfried ", "Riehl", "Male"),
    ("Barbara", "Bedner", "Female"),
    ("Robert ", "Thurner", "Male"),
    ("Sigrid", "Langhorst", "Female"),
    ("Dieter ", "Swoboda", "Male"),
    ("Lidia ", "Melchior", "Female"),
    ("Phil ", "Seidel", "Male"),
    ("Burkhard ", "Holthaus", "Male"),
    ("Irene ", "Kantel", "Female"),
    ("Frank ", "Hochholzer", "Male"),
    ("Noah ", "Rothert", "Male"),
    ("Victoria", "Bülskämper", "Female"),
    ("Maximilian ", "Hünniger", "Male"),
    ("Erwin ", "Baar", "Male"),
    ("Vanessa ", "Rothmund", "Female"),
    ("Jürgen ", "Frosch", "Male"),
    ("Joana ", "Knoth", "Female")
  )

  private val predefinedPricesAndStatuses = List(
    (1, 95, 72.60, "Legende"),
    (2, 85, 60.50, "Profi"),
    (3, 75, 48.40, "Halb-Profi"),
    (4, 65, 36.30, "Fortgeschritten"),
    (5, 55, 24.20, "Frischling"),
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

  def generateTutors(tuteeProfile: TuteeProfile, messages: Messages): List[TutorMatch] = {
    predefinedPricesAndStatuses.map { case (order, score, price, status) =>
      val index = Math.abs(tuteeProfile.userID.hashCode() + order)
      val tutor = predefinedTutors(index % predefinedTutors.size)
      val otherInterest = Interest.values.toList(index % Interest.values.size).toString
      val interest = messages(s"interest.${if (order <= 2) tuteeProfile.interest1 else otherInterest}")
      val subject = messages(s"subject.${tuteeProfile.subjectImprove1}")
      val gender = tutor._3
      val descriptionList = predefinedDescriptions(subject, interest, gender)
      val description = descriptionList(index % descriptionList.size)
      val avatar = if (gender == "Male") s"avatars/male-${index % 14 + 1}.jpeg" else s"avatars/female-${index % 9 + 1}.jpeg"
      TutorMatch(
        userID = UUID.randomUUID,
        firstName = tutor._1,
        lastName = tutor._2,
        description = description,
        avatarUrl = Some(avatar),
        price = price,
        subject1 = subject,
        interest = interest,
        matchingScore = score + index % 3,
        status = status,
        order = order,
        isFake = true
      )
    }
  }

}