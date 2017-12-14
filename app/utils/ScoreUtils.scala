package utils

object ScoreUtils {

  def percentageToScore(percentage: Double) =
    if (percentage < 0.2) 1
    else if (percentage < 0.4) 2
    else if (percentage < 0.6) 3
    else if (percentage < 0.8) 4
    else 5

  def reverseTuteeSubjectScore(score: Int) = score match {
    case 1 => 5
    case 2 => 4
    case 3 => 3
    case 4 => 2
    case 5 => 1
    case 6 => 1
  }

}
