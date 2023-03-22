package model

import controller.DictionarySize
import controller.DictionaryType
import controller.Strategy

data class Configuration(
  val dictionaryType: DictionaryType = DictionaryType.QLess,
  val dictionarySize: DictionarySize = DictionarySize.Small,
  val strategy: Strategy = Strategy.LongestFirst,
  val allowTouchingWords: Boolean = true,
  val allowDuplicateWords: Boolean = true,
) {
  companion object {
    val Default = Configuration()
  }
}
