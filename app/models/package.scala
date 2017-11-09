import models.enums.DiscType.DiscType
import models.enums.Language.Language
import models.enums.{ DiscType, Language, UserType }
import models.enums.UserType.UserType
import slick.jdbc.PostgresProfile.api._

package object models {

  implicit val userTypeMapper = MappedColumnType.base[UserType, String](
    e => e.toString,
    s => UserType.withName(s)
  )

  implicit val discCategoryTypeMapper = MappedColumnType.base[DiscType, String](
    e => e.toString,
    s => DiscType.withName(s)
  )

  implicit val languageTypeMapper = MappedColumnType.base[Language, String](
    e => e.toString,
    s => Language.withName(s)
  )

  implicit val dateMapper = MappedColumnType.base[java.util.Date, java.sql.Date](
    e => new java.sql.Date(e.getTime),
    s => new java.util.Date(s.getTime)
  )
}
