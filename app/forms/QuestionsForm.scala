package forms

import play.api.data.Form
import play.api.data.Forms._

object QuestionsForm {

  val form = Form(
    mapping(
      "page" -> number,
      "questions" -> list[Result](
        mapping(
          "questionId" -> number,
          "value" -> number,
          "answerId" -> optional(number)
        )(Result.apply)(Result.unapply)
      )
    )(Data.apply)(Data.unapply)

  )

  case class Data(page: Int, questions: List[Result])

  case class Result(questionId: Int, value: Int, answerId: Option[Int])
}

