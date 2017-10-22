package models.daos
import javax.inject.Inject

import models.Answer
import models.daos.AnswerDAOImpl._
import models.tables.AnswerTable
import play.api.db.slick.DatabaseConfigProvider
import slick.basic.DatabaseConfig
import slick.jdbc.{ JdbcBackend, JdbcProfile }
import slick.lifted.TableQuery

class AnswerDAOImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends AnswerDAO {

  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db

  import dbConfig.profile.api._

  override def saveAll(answers: Seq[Answer]) = {

    db.run(DBIO.sequence(answers.map(a => answersTable.insertOrUpdate(a))).transactionally)
  }
}

object AnswerDAOImpl {

  private val answersTable = TableQuery[AnswerTable]

}