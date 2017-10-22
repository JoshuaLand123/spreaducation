package models.daos

import models.Answer

import scala.concurrent.Future

trait AnswerDAO {

  def saveAll(answers: Seq[Answer]): Future[Seq[Int]]

}
