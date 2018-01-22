package utils.route

import java.time.format.DateTimeFormatter
import java.time.{ LocalDate, LocalDateTime }
import java.util.UUID

import play.api.mvc.{ PathBindable, QueryStringBindable }

import scala.util.Try

object Binders {

  val localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
  val localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  implicit object UUIDPathBindable extends PathBindable[UUID] {
    override def bind(key: String, value: String) = try {
      Right(UUID.fromString(value))
    } catch {
      case _: Exception => Left("Cannot parse parameter '" + key + "' with value '" + value + "' as UUID")
    }

    override def unbind(key: String, value: UUID): String = value.toString
  }

  implicit object LocalDateQueryStringBindable extends QueryStringBindable[LocalDate] {

    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, LocalDate]] =
      params.get(key).map(value => Try(LocalDate.parse(value.head)).toOption.toRight(s"Cannot parse date + ${value.head}"))

    override def unbind(key: String, date: LocalDate): String = key + "=" + date.format(localDateFormatter)

  }

  implicit object LocalDateTimeQueryStringBindable extends QueryStringBindable[LocalDateTime] {

    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, LocalDateTime]] =
      params.get(key).map(value => Try(LocalDateTime.parse(value.head)).toOption.toRight(s"Cannot parse datetime + ${value.head}"))

    override def unbind(key: String, date: LocalDateTime): String = key + "=" + date.format(localDateTimeFormatter)

  }
}
