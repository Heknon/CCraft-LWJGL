package window

import event.KeyEvent
import org.joml.Matrix4f
import org.joml.Matrix4fc
import org.joml.Vector3f
import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.*
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL
import render.MeshLoader
import render.Renderer
import render.Rotation
import render.entities.Entity
import kotlin.properties.Delegates


class Window(private val title: String = "CCraft", val width: Int = 1920, val height: Int = 1080) {

    private var window by Delegates.notNull<Long>()
    private val keyboard = Keyboard()

    init {
        if (INSTANCE == null) INSTANCE = this
    }

    fun run() {
        println("Running LWJGL Version ${Version.getVersion()}")

        init()
        loop()
    }

    private fun init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set()

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        check(glfwInit()) { "Unable to initialize GLFW" }

        // Configure GLFW
        glfwDefaultWindowHints() // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

        // Create the window
        window = glfwCreateWindow(width, height, title, NULL, NULL)
        if (window == NULL) throw RuntimeException("Failed to create the GLFW window")

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window) { window: Long, key: Int, scanCode: Int, action: Int, _: Int ->
            keyboard.onKeyPress(KeyEvent(window, action, key, scanCode))
        }

        registerKeyboardCalls()

        stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode!!.width() - pWidth[0]) / 2,
                    (vidmode.height() - pHeight[0]) / 2
            )
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(window)
        // Enable v-sync
        glfwSwapInterval(1)

        // Make the window visible
        glfwShowWindow(window)


    }

    private fun loop() {
        GL.createCapabilities()

        GL11.glClearColor(0.3f, 0.6f, 0.86f, 1.0f)

        val vertices = floatArrayOf(
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f
        )

        val indices = intArrayOf(
                0, 1, 2,
                2, 3, 0
        )

        val uv = floatArrayOf(
                0f, 0f,
                0f, 1f,
                1f, 1f,
                1f, 0f
        )

        val mesh = MeshLoader.createMesh(vertices, uv, indices).addTexture("a.png")
        val entity = Entity(mesh, Vector3f(0f, 0f, 0f), Rotation(0f, 0f, 0f), 1f)
        val renderer = Renderer()


        while (!glfwWindowShouldClose(window)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

            renderer.render(entity, "textured", false)

            glfwSwapBuffers(window)
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents()
        }
    }

    private fun registerKeyboardCalls() {

        keyboard.registerKeyCallback {
            if (it.key == GLFW_KEY_ESCAPE && it.action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(it.window, true)
                MeshLoader.cleanUp()
                Renderer.shaders.values.forEach { shader -> shader.cleanUp() }
            }
        }
    }

    companion object {
        var INSTANCE: Window? = null
    }
}