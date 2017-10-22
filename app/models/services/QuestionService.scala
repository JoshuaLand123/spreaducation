package models.services

import models.{ Answer, Question, User }

import scala.concurrent.Future

trait QuestionService {

  def retrieve(user: User): Future[Seq[(Question, Option[Answer])]]

}
