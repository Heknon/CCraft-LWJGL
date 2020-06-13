package game

import engine.IGameLogic
import engine.MouseInput
import engine.Window
import engine.render.MeshLoader
import engine.render.Renderer
import engine.render.WorldObject3D
import engine.render.lighting.PointLight
import engine.render.lighting.Sun
import engine.render.model.Camera
import engine.render.model.Cube
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11

class CCraft : IGameLogic {
    var lightIntensity = 20.0f
    val ambientLight = Vector3f(0.3f, 0.3f, 0.3f);
    var lightColor = Vector3f(1f, 1f, 1f)
    var lightPosition = Vector3f(0f, 0f, 1f)

    private val renderer: Renderer = Renderer()
    private var objects: MutableList<WorldObject3D> = mutableListOf()
    private val cameraInc: Vector3f = Vector3f()
    private val camera: Camera = Camera()
    private val sun: Sun = Sun(lightIntensity =  lightIntensity)
    private val pointLight: PointLight = PointLight(
            lightColor,
            lightPosition,
            lightIntensity,
            PointLight.Attenuation(0.6f, 0.075f, 0.3f)
    )

    override fun init() {
        renderer.init()
//        objects.add(WorldObject3D(MeshLoader.createMesh("models/grass.obj")))
        objects.add(Cube(Vector3f(2f, 0f, -4f)))
        objects.add(Cube(Vector3f(3f, 0f, -5f)))
        objects.add(Cube(Vector3f(3f, 0f, -4f)))
        objects.add(Cube(Vector3f(3f, 1f, -4f)))

        for (x in 0 until 10) {
            for (z in 0 until 10) {
                for (y in 0 until 10) {
                    objects.add(Cube(Vector3f(x.toFloat(), y.toFloat(), -z.toFloat())))
                }
            }
        }
    }

    override fun render(window: Window) {
        renderer.clear()

        if (window.resized) {
            window.setClearColor(135, 206, 235, 255)
            window.setClearColor(0, 0, 0, 0)
            GL11.glViewport(0, 0, window.size.width, window.size.height)
            renderer.updateProjectionMatrix(window)
            window.resized = false
        }

        renderer.render(objects, "textured", camera, ambientLight, pointLight, sun)
    }

    override fun update(interval: Float, mouseInput: MouseInput) {
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP)

        val rotVec = mouseInput.displVec
        camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0f)

        sun.update()
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

        if (window.isKeyPressed(GLFW_KEY_UP))
            pointLight.position.z++

        if (window.isKeyPressed(GLFW_KEY_DOWN))
            pointLight.position.z--

        if (window.isKeyPressed(GLFW_KEY_RIGHT))
            pointLight.position.x++

        if (window.isKeyPressed(GLFW_KEY_LEFT))
            pointLight.position.x--

        if (window.isKeyPressed(GLFW_KEY_PERIOD))
            pointLight.position.y++

        if (window.isKeyPressed(GLFW_KEY_COMMA))
            pointLight.position.y--

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