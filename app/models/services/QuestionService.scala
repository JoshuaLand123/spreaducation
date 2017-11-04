package models.services

import models.{ Answer, QuestionModel, User }

import scala.concurrent.Future

trait QuestionService {

  def retrieve(user: User, page: Int, limit: Int): Future[QuestionModel]

  def saveAnswers(answer: Seq[Answer]): Future[Seq[Int]]

}
