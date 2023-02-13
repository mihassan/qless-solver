package controller

import kotlinx.coroutines.await
import kotlinx.browser.window
import model.Dictionary

object DictionaryLoader {
  private const val DICTIONARY_PATH = "dictionary/words.txt"
  
  suspend fun loadDictionary(): Dictionary {
    val content =  window.fetch(DICTIONARY_PATH).await().text().await()
    return Dictionary.of(content)
  }
}
