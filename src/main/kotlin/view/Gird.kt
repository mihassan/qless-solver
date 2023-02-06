package view

import controller.DictionaryLoader
import controller.Solver
import csstype.*
import react.FC
import react.Props
import emotion.react.css
import kotlinx.coroutines.*
import model.Board
import model.Dictionary.Companion.emptyDictionary
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.tbody
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.tr
import react.useState

external interface GridProps : Props {
    var letters: List<List<String>>
}

val Grid = FC<GridProps> { props ->
    var letters by useState { props.letters }
    var loaded by useState { false }
    var dictionary by useState { emptyDictionary() }

    table {
        tbody {
            letters.forEach { row ->
                tr {
                    row.forEach { letter ->
                        td {
                            css {
                                fontFamily = FontFamily.monospace
                                padding = 5.px
                                color = rgb(31, 63, 0)
                                backgroundColor = rgb(167, 167, 167)
                            }
                            +letter
                        }
                    }
                }
            }
        }
    }

    if (loaded) {
        div {
            p {
                +"Loaded dictionary with ${dictionary.words.size} words."
            }
        }
    } else {
        div {
            p {
                +"Loading..."
            }
        }
    }

    GlobalScope.launch(Dispatchers.Default) {
        if(!loaded) {
            val dictionary = DictionaryLoader.loadDictionary()
            val solver = Solver(dictionary)
            val board: Board? = solver.solve("SKALDTXLANHD".toCharArray().toList())
            if (board != null) {
                letters = board.lines().map { it.map { "$it" } }
            } else {
                console.log("Could not solve.")
            }
            loaded = true
        }
    }
}
