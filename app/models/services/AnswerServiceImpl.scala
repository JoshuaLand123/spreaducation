package models.services
import javax.inject.Inject

import models.Answer
import models.daos.AnswerDAO

class AnswerServiceImpl @Inject() (answerDAO: AnswerDAO) extends AnswerService {

  override def saveAll(answers: Seq[Answer]) = answerDAO.saveAll(answers)
}
