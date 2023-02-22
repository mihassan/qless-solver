package view

import csstype.*
import js.core.jso
import mui.material.PaletteMode.dark
import mui.material.PaletteMode.light
import mui.material.styles.createTheme

object Themes {
  val Light = createTheme(
    jso {
      palette = jso {
        mode = light
        primary = jso {
          main = Color("#1a237e")
        }
        secondary = jso {
          main = Color("#f50057")
        }
      }
      typography = jso {
        fontSize = 20
      }
    }
  )

  val Dark = createTheme(
    jso {
      palette = jso {
        mode = dark
        primary = jso {
          main = Color("#1a237e")
        }
        secondary = jso {
          main = Color("#f50057")
        }
      }
      typography = jso {
        fontSize = 20
      }
    }
  )
}
