import models.UserType.UserType

import slick.jdbc.PostgresProfile.api._

package object models {

  implicit val userTypeMapper = MappedColumnType.base[UserType, String](
    e => e.toString,
    s => UserType.withName(s)
  )
}
