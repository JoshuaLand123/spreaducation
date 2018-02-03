package models.enums

import play.api.i18n.Messages

object OccupationDegree extends Enumeration {
  type OccupationDegree = Value
  val Middle, HighSchool, Diplima, Bachelor, Master, Doctor, Professor = Value

  def selectList(messages: Messages) =
    OccupationDegree.values.toSeq
      .map(g => g.toString -> messages("occupationDegree." + g.toString))
      .sortBy(_._2) :+ ("Other" -> messages("occupationDegree.Other"))
}
