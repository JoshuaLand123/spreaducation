package models.services

import javax.inject.Inject

import models.User
import models.daos.QuestionDAO

class QuestionServiceImpl @Inject() (questionDAO: QuestionDAO) extends QuestionService {

  override def retrieve(user: User, page: Int, limit: Int) = questionDAO.find(user, page, limit)
}