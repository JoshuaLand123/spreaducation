package models.services

import javax.inject.Inject

import models.{ Answer, User }
import models.daos.QuestionDAO

class QuestionServiceImpl @Inject() (questionDAO: QuestionDAO) extends QuestionService {

  override def retrieve(user: User, page: Int, limit: Int) = questionDAO.find(user, page, limit)

  override def saveAnswers(answers: Seq[Answer]) = questionDAO.saveAnswers(answers)
}