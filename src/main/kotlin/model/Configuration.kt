package model

import controller.DictionarySize
import controller.DictionaryType
import controller.Strategy

data class Configuration(
  val dictionaryType: DictionaryType = DictionaryType.QLess,
  val dictionarySize: DictionarySize = DictionarySize.Small,
  val strategy: Strategy = Strategy.LongestFirst,
) {
  companion object {
    val Default = Configuration()
  }
}
