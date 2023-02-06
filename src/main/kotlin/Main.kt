import kotlinx.browser.document
import react.create
import react.dom.client.createRoot
import view.App

fun main() {
    val container = document.createElement("div")
    createRoot(container).render(App.create())
    document.body!!.appendChild(container)
}
