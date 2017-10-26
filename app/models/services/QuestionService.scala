package models.services

import models.{ QuestionModel, User }

import scala.concurrent.Future

trait QuestionService {

  def retrieve(user: User, page: Int, limit: Int): Future[QuestionModel]

}
