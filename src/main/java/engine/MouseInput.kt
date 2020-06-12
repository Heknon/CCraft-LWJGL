package engine

import org.joml.Vector2d
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW


class MouseInput {
    private val previousPos: Vector2d = Vector2d(-1.0, -1.0)
    private val currentPos: Vector2d = Vector2d(0.0, 0.0)
    val displVec: Vector2f = Vector2f()
    private var inWindow = false
    var isLeftButtonPressed = false
        private set
    var isRightButtonPressed = false
        private set

    fun init(window: Window) {
        // hide and lock cursor
        GLFW.glfwSetInputMode(window.handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED)

        GLFW.glfwSetCursorPosCallback(window.handle) { _: Long, xpos: Double, ypos: Double ->
            currentPos.x = xpos
            currentPos.y = ypos
        }
        GLFW.glfwSetCursorEnterCallback(window.handle) { _: Long, entered: Boolean -> inWindow = entered }
        GLFW.glfwSetMouseButtonCallback(window.handle) { _: Long, button: Int, action: Int, _: Int ->
            isLeftButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS
            isRightButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS
        }
    }

    fun input(window: Window?) {
        displVec.x = 0f
        displVec.y = 0f
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            val deltax = currentPos.x - previousPos.x
            val deltay = currentPos.y - previousPos.y
            val rotateX = deltax != 0.0
            val rotateY = deltay != 0.0
            if (rotateX) {
                displVec.y = deltax.toFloat()
            }
            if (rotateY) {
                displVec.x = deltay.toFloat()
            }
        }
        previousPos.x = currentPos.x
        previousPos.y = currentPos.y
    }

}