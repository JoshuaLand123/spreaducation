package models.enums

import play.api.i18n.Messages

object LessonType extends Enumeration {
  type LessonType = Value
  val Online, Offline, Both = Value

  def selectList(messages: Messages) =
    LessonType.values.toSeq
      .map(g => g.toString -> messages("lessonType." + g.toString))
      .sortBy(_._2)
}
