package models.enums

import play.api.i18n.Messages

object Language extends Enumeration {
  type Language = Value
  val Afrikaans, Albanian, Amharic, Arabic, Aramaic, Armenian, Assamese, Aymara, Azerbaijani, Balochi, Bamanankan,
  BashkortBashkir, Basque, Belarusan, Bengali, Bhojpuri, Bislama, Bosnian, Brahui, Bulgarian, Burmese, Cantonese,
  Catalan, Cebuano, Chechen, Cherokee, Chinese, Croatian, Czech, Dakota, Danish, Dari, Dholuo, Dutch, English,
  Esperanto, Estonian, Ewe, Finnish, French, Georgian, German, Gikuyu, Greek, Guarani, Gujarati, HaitianCreole, Hausa,
  Hawaiian, HawaiianCreole, Hebrew, Hiligaynon, Hindi, Hungarian, Icelandic, Igbo, Ilocano, Indonesian, InuitOrInupiaq,
  IrishGaelic, Italian, Japanese, Jarai, Javanese, Kiche, Kabyle, Kannada, Kashmiri, Kazakh, Khmer, KhoekhoeKorean,
  Kurdish, Kyrgyz, Lao, Latin, Latvian, Lingala, Lithuanian, Macedonian, Maithili, Malagasy, Malay, Malayalam, Mandarin,
  Marathi, Mende, Mongolian, Nahuatl, Navajo, Nepali, Norwegian, Ojibwa, Oriya, Oromo, Pashto, Persian, Polish,
  Portuguese, Punjabi, Quechua, Romani, Romanian, Russian, Rwanda, Samoan, Sanskrit, SerbianShona, Sindhi, Sinhala,
  Slovak, Slovene, Somali, Spanish, Swahili, Swedish, Tachelhit, Tagalog, Tajiki, Tamil, Tatar, Telugu, Thai, Tibetic,
  Tigrigna, TokPisin, Turkish, Turkmen, Ukrainian, Urdu, Uyghur, Uzbek, Vietnamese, Warlpiri, Welsh, Wolof, Xhosa,
  Yakut, Yiddish, Yoruba, Yucatec, Zapotec, Zulu = Value

  def selectList(messages: Messages) =
    Language.values.toSeq
      .map(g => g.toString -> messages("language." + g.toString))
      .sortBy(_._2) :+ ("Other" -> messages("language.Other"))
}
