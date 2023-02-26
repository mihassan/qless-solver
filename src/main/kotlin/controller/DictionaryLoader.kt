package controller

import kotlinx.coroutines.await
import kotlinx.browser.window
import model.Dictionary

enum class DictionarySize {
  Small, Medium, Large, Huge
}

object DictionaryLoader {
  suspend fun loadDictionary(size: DictionarySize): Dictionary {
    val content =  window.fetch(getDictionaryPath(size)).await().text().await()
    return Dictionary.of(content)
  }

  private fun getDictionaryPath(size: DictionarySize): String {
    return "dictionary/${size}.txt"
  }
}
