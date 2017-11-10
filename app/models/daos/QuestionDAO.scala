package models.daos

import java.util.UUID

import models.{ Answer, QuestionModel, User }

import scala.concurrent.Future

trait QuestionDAO {

  def find(user: User, page: Int, limit: Int): Future[QuestionModel]

  def saveAnswers(answers: Seq[Answer]): Future[Seq[Int]]

  def getPsychoSubcategoryScores(userID: UUID): Future[Seq[(String, String, Double)]]

}
