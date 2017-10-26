package models.daos

import javax.inject.Inject

import models.daos.QuestionDAOImpl._
import models.tables._
import models.{ QuestionModel, User }
import play.api.db.slick.DatabaseConfigProvider
import slick.basic.DatabaseConfig
import slick.jdbc.{ JdbcBackend, JdbcProfile }
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class QuestionDAOImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends QuestionDAO {

  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db

  import dbConfig.profile.api._

  override def find(user: User, page: Int, limit: Int): Future[QuestionModel] = {
    val query = questions
      .filter(_.questionUserType === user.userType)
      .joinLeft(answers)
      .on((q, a) => q.id === a.questionId && a.userId === user.userID)
      .sortBy(_._1.questionOrder)
    db.run(query.result).map(x => QuestionModel(x.slice((page - 1) * limit, page * limit), x.size))
  }

}

object QuestionDAOImpl {

  private val questions = TableQuery[QuestionTable]
  private val answers = TableQuery[AnswerTable]

}