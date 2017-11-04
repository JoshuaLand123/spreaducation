package modules

import com.google.inject.AbstractModule
import models.daos._
import models.services._
import net.codingwell.scalaguice.ScalaModule

/**
 * The base Guice module.
 */
class BaseModule extends AbstractModule with ScalaModule {

  /**
   * Configures the module.
   */
  def configure(): Unit = {
    bind[AuthTokenDAO].to[AuthTokenDAOImpl]
    bind[AuthTokenService].to[AuthTokenServiceImpl]
    bind[QuestionDAO].to[QuestionDAOImpl]
    bind[QuestionService].to[QuestionServiceImpl]
  }
}
