package models.daos

import models.{ Answer, QuestionModel, User }

import scala.concurrent.Future

trait QuestionDAO {

  def find(user: User, page: Int, limit: Int): Future[QuestionModel]

  def saveAnswers(answers: Seq[Answer]): Future[Seq[Int]]

}
