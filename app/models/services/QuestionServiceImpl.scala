package models.services

import java.util.UUID
import javax.inject.Inject

import models.daos.QuestionDAO
import models.{ Answer, User }

import scala.concurrent.ExecutionContext.Implicits.global

class QuestionServiceImpl @Inject() (questionDAO: QuestionDAO) extends QuestionService {

  override def retrieve(user: User, page: Int, limit: Int) = questionDAO.find(user, page, limit)

  override def saveAnswers(answers: Seq[Answer]) = questionDAO.saveAnswers(answers)

  override def getPsychoSubcategoryScores(userID: UUID) = questionDAO.getPsychoSubcategoryScores(userID)

  override def isAllowedToEditQuestions(user: User) = questionDAO.answeredAllQuestions(user).map(!_)
}