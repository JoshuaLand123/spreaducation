package models.enums

import play.api.i18n.Messages

object OccupationDegree extends Enumeration {
  type OccupationDegree = Value
  val Middle = Value("Middle")
  val HighSchool = Value("HighSchool")
  val Diplima = Value("Diplima")
  val Bachelor = Value("Bachelor")
  val Master = Value("Master")
  val Doctor = Value("Doctor")
  val Professor = Value("Professor")
  val Other = Value("Other")

  def selectList(messages: Messages) =
    OccupationDegree.values.toSeq.map(g => g.toString -> messages("occupationDegree." + g.toString))
}
