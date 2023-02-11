package controller

import kotlinx.coroutines.await
import kotlinx.js.fetch
import model.Dictionary

object DictionaryLoader {
  suspend fun loadDictionary(): Dictionary {
    val content = fetch("./dictionary/small.txt").text().await()
    return Dictionary.of(content)
  }
}
