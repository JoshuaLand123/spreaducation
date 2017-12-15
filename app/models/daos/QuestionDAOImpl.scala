package models.daos

import java.util.UUID
import javax.inject.Inject

import models.daos.QuestionDAOImpl._
import models.tables._
import models.{ Answer, QuestionModel, User }
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
    val query = questionsTable
      .filter(_.questionUserType === user.userType)
      .joinLeft(answersTable).on((q, a) => q.id === a.questionId && a.userId === user.userID)
      .sortBy(_._1.questionOrder)
    db.run(query.result).map(x => QuestionModel(x.slice((page - 1) * limit, page * limit), x.size))
  }

  override def saveAnswers(answers: Seq[Answer]) = {

    db.run(DBIO.sequence(answers.map(a => answersTable.insertOrUpdate(a))).transactionally)
  }

  override def getPsychoSubcategoryScores(userID: UUID): Future[Seq[(String, String, Double)]] = {
    val userIdString = userID.toString
    val query = sql"""select
                        q.psycho_category,
                        q.psycho_subcategory,
                        round (sum(case
                                    when a.score = 1 then case when q.psycho_score_reverse then 1 else 0 end
                                    when a.score = 2 then case when q.psycho_score_reverse then 0.75 else 0.25 end
                                    when a.score = 3 then 0.5
                                    when a.score = 4 then case when q.psycho_score_reverse then 0.25 else 0.75 end
                                    else case when q.psycho_score_reverse then 0 else 1 end
                                end) / count(1), 2) as score
                     from answers a
                     join questions q on q.id = a.question_id
                     where a.user_id = '#$userIdString'
                     group by q.psycho_category, q. psycho_subcategory
                     order by q.psycho_category
           """.as[(String, String, Double)]

    db.run(query)
  }

  override def answeredAllQuestions(user: User): Future[Boolean] = {
    val totalQuestions = db.run(questionsTable.filter(_.questionUserType === user.userType).size.result)
    val answeredQuestions = db.run(answersTable.filter(_.userId === user.userID).size.result)
    totalQuestions.flatMap(total => answeredQuestions.map(answered => answered == total))

  }

}

object QuestionDAOImpl {

  private val questionsTable = TableQuery[QuestionTable]
  private val answersTable = TableQuery[AnswerTable]

}