package controller

import kotlinx.coroutines.await
import kotlinx.browser.window
import model.Dictionary

enum class DictionaryType {
  General, QLess
}

enum class DictionarySize {
  Small, Medium, Large, Huge, Enormous
}

class DictionaryLoader(type: DictionaryType, size: DictionarySize) {
  private val dictionaryPath: String =
    "https://raw.githubusercontent.com/mihassan/qless-solver/main/dictionary/$type/$size.txt"

  suspend fun load(): Dictionary {
    val content = window.fetch(dictionaryPath).await().text().await()
    return Dictionary.of(content)
  }
}
