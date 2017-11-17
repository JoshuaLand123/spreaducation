package models.enums

import play.api.i18n.Messages

object Language extends Enumeration {
  type Language = Value
  val Arabic = Value("Arabic")
  val English = Value("English")
  val French = Value("French")
  val German = Value("German")
  val Italian = Value("Italian")
  val Russian = Value("Russian")
  val Turkish = Value("Turkish")

  def selectList(messages: Messages) =
    Language.values.toSeq.map(g => g.toString -> messages("language." + g.toString)).sortBy(_._2) :+ ("Other" -> messages("language.Other"))
}
