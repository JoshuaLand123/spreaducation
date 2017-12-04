package modules

import play.api.libs.json._

case class PsychogramCategoryData(subcategories: List[PsychogramSubcategoryData], description: String)
case class PsychogramSubcategoryData(name: String, score: Int, description: String)

object PsychogramData {

  implicit val psychogramSubcategoryDataFormat = Json.format[PsychogramSubcategoryData]
  implicit val psychogramCategoryDataFormat = Json.format[PsychogramCategoryData]

}