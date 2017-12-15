package models.services

import java.util.UUID

import models.{ Answer, QuestionModel, User }

import scala.concurrent.Future

trait QuestionService {

  def retrieve(user: User, page: Int, limit: Int): Future[QuestionModel]

  def saveAnswers(answer: Seq[Answer]): Future[Seq[Int]]

  def getPsychoSubcategoryScores(userID: UUID): Future[Seq[(String, String, Double)]]

  def isAllowedToEditQuestions(user: User): Future[Boolean]

}
