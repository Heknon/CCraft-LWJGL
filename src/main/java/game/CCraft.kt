package game

import engine.IGameLogic
import engine.MouseInput
import engine.Window
import engine.model.Camera
import engine.model.Cube
import engine.render.MeshLoader
import engine.render.Renderer
import engine.render.WorldObject3D
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11

class CCraft : IGameLogic {
    private val renderer: Renderer = Renderer()
    private var objects: MutableList<WorldObject3D> = mutableListOf()
    private val cameraInc: Vector3f = Vector3f()
    private val camera: Camera = Camera()

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

        renderer.render(objects, "textured", camera)
    }

    override fun update(interval: Float, mouseInput: MouseInput) {
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP)

        val rotVec = mouseInput.displVec
        camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0f)
    }

    override fun input(window: Window, mouseInput: MouseInput) {
        cameraInc.set(0.0, 0.0, 0.0)

        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z -= 1
        }

        if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z += 1
        }

        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x -= 1
        }

        if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x += 1
        }

        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            cameraInc.y -= 1
        }

        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            cameraInc.y += 1
        }

    }

    override fun cleanup() {
        renderer.cleanup()
        MeshLoader.cleanup()
    }

    companion object {
        private const val MOUSE_SENSITIVITY = 0.2f
        private const val CAMERA_POS_STEP = 0.05f
    }
}