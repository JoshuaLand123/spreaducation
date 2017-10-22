package models.daos

import models.{ Answer, Question, User }

import scala.concurrent.Future

trait QuestionDAO {

  def find(user: User): Future[Seq[(Question, Option[Answer])]]

}
