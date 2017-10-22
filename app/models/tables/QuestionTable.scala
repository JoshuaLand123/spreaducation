package models.tables

import models.Question
import models.UserType.UserType
import slick.jdbc.PostgresProfile.api._

class QuestionTable(tag: Tag) extends Table[Question](tag, "questions") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def questionText = column[String]("question_text")

  def questionUserType = column[UserType]("question_user_type")

  def questionOrder = column[Int]("question_order")

  override def * = (id, questionText, questionUserType, questionOrder) <> (Question.tupled, Question.unapply)

}
