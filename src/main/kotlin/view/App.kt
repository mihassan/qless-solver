package view

import kotlinx.coroutines.MainScope
import react.FC
import react.Props
import react.create
import react.useState

enum class AppState {
  Started,
  Loading,
  Loaded
}

val App = FC<Props> {
  val mainScope = MainScope()
  val state by useState { AppState.Started }

  +Banner.create()
  +Grid.create {
    letters = "ABC\nDEF\nGHI".lines().map { it.toCharArray().map { "$it" } }
  }
}