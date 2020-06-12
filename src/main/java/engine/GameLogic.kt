package engine

import engine.render.Renderer
import org.lwjgl.opengl.GL11.glViewport

class GameLogic {
    private val renderer: Renderer = Renderer()

    fun init() {
        renderer.init()
    }

    fun render(window: Window) {
        if (window.resized) {
            glViewport(0, 0, window.size.width, window.size.height)
            window.resized = false
        }

        renderer.clear()
    }

    fun update(interval: Float) {

    }

    fun input(window: Window) {

    }
}