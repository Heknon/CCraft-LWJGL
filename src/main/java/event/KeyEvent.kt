package event

data class KeyEvent(override val window: Long, override val action: Int, val key: Int, val scanCode: Int) : Event(window, action)