package controller

import kotlinx.coroutines.await
import kotlinx.js.fetch
import model.Dictionary

object DictionaryLoader {
  private const val DICTIONARY_PATH = "dictionary/words.txt"
  
  suspend fun loadDictionary(): Dictionary {
    val content = fetch(DICTIONARY_PATH).text().await()
    return Dictionary.of(content)
  }
}
