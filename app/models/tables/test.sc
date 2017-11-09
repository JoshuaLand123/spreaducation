import models.enums.Language

val b = Language.values.toSeq.map(l => ("language."+l.toString()) -> ("language."+l.toString()))