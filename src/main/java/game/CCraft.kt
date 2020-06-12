package game

import engine.IGameLogic
import engine.Window
import engine.model.Cube
import engine.render.Renderer
import engine.render.WorldObject3D
import org.joml.Vector3f
import org.lwjgl.opengl.GL11

class CCraft : IGameLogic {
    private val renderer: Renderer = Renderer()
    private var objects: MutableList<WorldObject3D> = mutableListOf()

    override fun init() {
        renderer.init()
        objects.add(Cube(Vector3f(0f, 0f, -4f)))
        objects.add(Cube(Vector3f(3f, 0f, -4f)))
    }

    override fun render(window: Window) {
        renderer.clear()

        if (window.resized) {
            window.setClearColor(135, 206, 235, 255)
            GL11.glViewport(0, 0, window.size.width, window.size.height)
            renderer.updateProjectionMatrix(window)
            window.resized = false
        }

        renderer.render(objects, "textured")
    }

    override fun update(interval: Float) {

    }

    override fun input(window: Window) {

    }

    override fun cleanup() {
        renderer.cleanup()
    }
}