package models.enums

import play.api.i18n.Messages

object Subject extends Enumeration {
  type Subject = Value
  val Math, German, English, French, Spanish, Latin, Italian, Chinese, Politics, History, Art, Music, Physics,
  Chemistry, Geography, Biology, ComputerScience = Value

  def selectList(messages: Messages) =
    Subject.values.toSeq
      .map(g => g.toString -> messages("subject." + g.toString))
      .sortBy(_._2) :+ ("Other" -> messages("subject.Other"))
}
