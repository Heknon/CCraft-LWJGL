package game

import engine.IGameLogic
import engine.MouseInput
import engine.Window
import engine.render.MeshLoader
import engine.render.Renderer
import engine.render.WorldObject3D
import engine.render.lighting.LightMaterial
import engine.render.lighting.PointLight
import engine.render.lighting.Sun
import engine.render.model.Camera
import game.world.Block
import game.world.Chunk
import game.world.Location
import game.world.OpenSimplexNoise
import org.joml.Random
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11
import java.util.concurrent.ThreadLocalRandom
import javax.swing.Spring.height
import javax.swing.Spring.width


class CCraft : IGameLogic {
    var lightIntensity = 20.0f
    val ambientLight = Vector3f(0.3f, 0.3f, 0.3f);
    var lightColor = Vector3f(1f, 1f, 1f)
    var lightPosition = Vector3f(0f, 0f, 1f)
    val lightMaterial = LightMaterial()

    private val renderer: Renderer = Renderer()
    private var objects: MutableList<WorldObject3D> = mutableListOf()
    private val cameraInc: Vector3f = Vector3f()
    private val camera: Camera = Camera()
    private val sun: Sun = Sun(lightIntensity = lightIntensity)
    private val pointLight: PointLight = PointLight(
            lightColor,
            lightPosition,
            lightIntensity,
            PointLight.Attenuation(0.6f, 0.075f, 0.3f)
    )

    override fun init() {
        renderer.init()

        val rand = ThreadLocalRandom.current()
        val generator = OpenSimplexNoise(rand.nextLong(Random.newSeed(), Long.MAX_VALUE))
        for (i in 0 until 50) {
            for (j in 0 until 50) {
                val blocks: MutableList<Block> = mutableListOf()
                val origin = Location(i * 16f, 0f, j * 16f)

                for (x in 0 until 16) {
                    for (z in 0 until 16) {
                        val nx: Double = (x + i * 16f) / 16.0 - 0.5
                        val nz: Double = (z + j * 16f) / 16.0 - 0.5
                        blocks.add(Block(0, Location(x.toFloat(), (generator.eval(nx, nz)) * 16, z.toFloat()), lightMaterial))
                    }
                }


                val chunk = Chunk(origin, blocks.toTypedArray())
                objects.add(chunk)
                println("chunk added")
            }
        }
        println("FINISHED INIT")

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
        private const val CAMERA_POS_STEP = 1f
    }
}