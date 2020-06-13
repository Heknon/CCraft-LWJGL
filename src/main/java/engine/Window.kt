package engine

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL
import kotlin.properties.Delegates


class Window(private val title: String = "CCraft", val size: Size, internal var vSync: Boolean = true) {

    var handle by Delegates.notNull<Long>()
        private set

    var resized: Boolean = true


    internal fun init() {

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set()

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        check(glfwInit()) { "Unable to initialize GLFW" }

        // Configure GLFW
        glfwDefaultWindowHints() // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

        if (System.getProperty("os.name").contains("Mac")) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
        }

        // Create the window
        handle = glfwCreateWindow(size.width, size.height, title, NULL, NULL)
        if (handle == NULL) throw RuntimeException("Failed to create the GLFW window")

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(handle) { window: Long, key: Int, scanCode: Int, action: Int, _: Int ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true)
        }

        glfwSetFramebufferSizeCallback(handle) { _, width, height ->
            this.size.width = width
            this.size.height = height
            resized = true
        }

        stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(handle, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())

            // Center the window
            glfwSetWindowPos(
                    handle,
                    (vidmode!!.width() - pWidth[0]) / 2,
                    (vidmode.height() - pHeight[0]) / 2
            )
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(handle)

        if (vSync) {
            glfwSwapInterval(1)
        }

        // Make the window visible
        glfwShowWindow(handle)

        GL.createCapabilities()

        glEnable(GL_DEPTH_TEST)

        glClearColor(0f, 0f, 0f, 0f)
    }


    /**
     * alias to set screen clear color
     */
    fun setClearColor(red: Float, green: Float, blue: Float, alpha: Float) = glClearColor(red, green, blue, alpha)

    /**
     * Use RGB to set screen clear color
     */
    fun setClearColor(red: Int, green: Int, blue: Int, alpha: Int) = setClearColor(red / 255f, green / 255f, blue / 255f, alpha / 255f)

    fun shouldClose(): Boolean = glfwWindowShouldClose(handle)

    fun isKeyPressed(key: Int): Boolean = glfwGetKey(handle, key) == GLFW_PRESS


    fun isKeyReleased(key: Int): Boolean = glfwGetKey(handle, key) == GLFW_RELEASE


    fun update() {
        glfwSwapBuffers(handle)
        glfwPollEvents()
    }

    data class Size(var width: Int = 1920, var height: Int = 1080) {
        val aspectRatio: Float get() = width.toFloat() / height.toFloat()
    }
}