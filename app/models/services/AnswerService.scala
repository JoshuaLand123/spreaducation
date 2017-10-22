package models.services

import models.Answer

import scala.concurrent.Future

trait AnswerService extends {

  def saveAll(answer: Seq[Answer]): Future[Seq[Int]]

}
