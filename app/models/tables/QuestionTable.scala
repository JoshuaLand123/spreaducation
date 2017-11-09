package models.tables

import models.enums.DiscType.DiscType
import models.Question
import models.enums.UserType.UserType
import slick.jdbc.PostgresProfile.api._

class QuestionTable(tag: Tag) extends Table[Question](tag, "questions") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def questionText = column[String]("question_text")

  def questionUserType = column[UserType]("question_user_type")

  def discCategory = column[DiscType]("disc_category")

  def psychoCategory = column[Option[String]]("psycho_category")

  def psychoSubcategory = column[Option[String]]("psycho_subcategory")

  def psychoScoreReverse = column[Option[Boolean]]("psycho_score_reverse")

  def questionOrder = column[Int]("question_order")

  override def * = (id, questionText, questionUserType, discCategory, psychoCategory, psychoSubcategory, psychoScoreReverse, questionOrder) <> (Question.tupled, Question.unapply)

}
