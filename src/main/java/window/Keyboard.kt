package window

import event.KeyEvent

class Keyboard {
    private val callbacks: MutableList<(KeyEvent) -> Unit> = mutableListOf()

    internal fun onKeyPress(event: KeyEvent) {
        callbacks.forEach { it(event) }
    }

    fun registerKeyCallback(callback: (KeyEvent) -> Unit) {
        callbacks.add(callback)
    }
}