import react.create
import react.dom.client.createRoot
import view.App
import web.dom.document
import web.html.HTML.div

fun main() {
  val root = document.createElement(div)
    .also { document.body.appendChild(it) }

  createRoot(root)
    .render(App.create())
}
