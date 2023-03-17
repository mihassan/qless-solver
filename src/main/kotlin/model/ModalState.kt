package model

enum class ModalState {
  NONE, DRAWER, HELP_DIALOG;

  fun toggleDrawer(): ModalState = when(this) {
    NONE -> DRAWER
    DRAWER -> NONE
    HELP_DIALOG -> DRAWER
  }

  fun openDrawer(): ModalState = when(this) {
    NONE -> DRAWER
    DRAWER -> DRAWER
    HELP_DIALOG -> DRAWER
  }

  fun closeDrawer(): ModalState = when(this) {
    NONE -> NONE
    DRAWER -> NONE
    HELP_DIALOG -> HELP_DIALOG
  }

  fun openHelpDialog(): ModalState = when(this) {
    NONE -> HELP_DIALOG
    DRAWER -> HELP_DIALOG
    HELP_DIALOG -> NONE
  }

  fun closeHelpDialog(): ModalState = when(this) {
    NONE -> HELP_DIALOG
    DRAWER -> HELP_DIALOG
    HELP_DIALOG -> NONE
  }
}
