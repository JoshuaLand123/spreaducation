import java.sql.Timestamp
import java.time.format.DateTimeFormatter
import java.time.{ LocalDate, LocalDateTime }

import models.enums.DiscType.DiscType
import models.enums.Language.Language
import models.enums.LessonType.LessonType
import models.enums.EventType.EventType
import models.enums.UserType.UserType
import models.enums._
import play.api.libs.json.{ Format, JsSuccess, JsValue, Json }
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

  implicit val eventTypeMapper = MappedColumnType.base[EventType, String](
    e => e.toString,
    s => EventType.withName(s)
  )

  implicit val dateMapper = MappedColumnType.base[java.util.Date, java.sql.Timestamp](
    e => new Timestamp(e.getTime),
    s => new java.util.Date(s.getTime)
  )

  implicit val localDateTimeMapper = MappedColumnType.base[java.time.LocalDateTime, java.sql.Timestamp](
    e => Timestamp.valueOf(e),
    s => s.toLocalDateTime
  )

  implicit val localDateMapper = MappedColumnType.base[java.time.LocalDate, java.sql.Timestamp](
    e => Timestamp.valueOf(e.atStartOfDay()),
    s => s.toLocalDateTime.toLocalDate
  )

  implicit val lessonTypeMapper = MappedColumnType.base[LessonType, String](
    e => e.toString,
    s => LessonType.withName(s)
  )

  implicit object JsonLocalDateFormatter extends Format[LocalDate] {
    val dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    override def writes(date: LocalDate) = Json.toJson(date.format(dateTimeformatter))
    override def reads(j: JsValue) = JsSuccess(LocalDate.parse(j.as[String]))
  }

  implicit object JsonLocalDateTimeFormatter extends Format[LocalDateTime] {
    val dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    override def writes(date: LocalDateTime) = Json.toJson(date.format(dateTimeformatter))
    override def reads(j: JsValue) = JsSuccess(LocalDateTime.parse(j.as[String]))
  }
}
