package models.enums

import play.api.i18n.Messages

object LessonType extends Enumeration {
  type LessonType = Value
  val Online = Value("Online")
  val Offline = Value("Offline")
  val Both = Value("Both")

  def selectList(messages: Messages) =
    LessonType.values.toSeq.map(g => g.toString -> messages("lessonType." + g.toString)).sortBy(_._2)
}
