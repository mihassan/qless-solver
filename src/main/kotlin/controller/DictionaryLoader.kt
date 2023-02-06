package controller

import kotlinx.coroutines.await
import kotlinx.coroutines.delay
import kotlinx.js.fetch
import model.Dictionary

object DictionaryLoader {
    suspend fun loadDictionary(): Dictionary {
        val content = fetch("./dictionary/small.txt").text().await()
        delay(2000)
        return Dictionary.from(content)
    }
}
