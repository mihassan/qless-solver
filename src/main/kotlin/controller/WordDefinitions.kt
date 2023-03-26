package controller

import kotlinx.browser.window
import kotlinx.coroutines.await

object WordDefinitions {
  private val apiPath: String = "https://api.dictionaryapi.dev/api/v2/entries/en/"

  suspend fun fetch(word: String): String? {
    val content = window.fetch(apiPath + word).await().text().await()
    // TODO(Better parsing of json response)
    return if ("No Definitions Found" in content)
      null
    else content
      .substringAfter("definition\":\"")
      .substringBefore("\"")
      .takeIf { it.isNotBlank() }
  }
}
