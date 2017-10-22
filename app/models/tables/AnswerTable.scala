package models.tables

import java.util.UUID

import models.Answer
import slick.jdbc.PostgresProfile.api._

class AnswerTable(tag: Tag) extends Table[Answer](tag, "answers") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def userId = column[UUID]("user_id")

  def questionId = column[Int]("question_id")

  def score = column[Int]("score")

  override def * = (id.?, userId, questionId, score) <> (Answer.tupled, Answer.unapply)

}