package models.daos

import models.{ QuestionModel, User }

import scala.concurrent.Future

trait QuestionDAO {

  def find(user: User, page: Int, limit: Int): Future[QuestionModel]

}
