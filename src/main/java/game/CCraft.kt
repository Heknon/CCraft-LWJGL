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
import game.world.Block
import game.world.Location
import game.world.World
import game.world.WorldImpl
import game.world.chunk.Chunk
import game.world.generator.OpenSimplexNoise
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.joml.Random
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.floor


class CCraft : IGameLogic {
    var lightIntensity = 20.0f
    val ambientLight = Vector3f(0.3f, 0.3f, 0.3f);
    var lightColor = Vector3f(1f, 1f, 1f)
    var lightPosition = Vector3f(0f, 0f, 1f)


    private val world: World = WorldImpl()
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

    override fun init(window: Window) {
        renderer.init()

        val rand = ThreadLocalRandom.current()
        val generator = OpenSimplexNoise(rand.nextLong(Random.newSeed(), Long.MAX_VALUE))
        val featureSize = 40.0

        executorService.submit {
            val usedLocations: MutableSet<Location<Int>> = ConcurrentHashMap.newKeySet()

            while (!window.shouldClose()) {
                val xMin = ((camera.position.x - PRE_RENDER_DISTANCE) / Chunk.CHUNK_X_SIZE).toInt()
                val zMin = ((camera.position.z - PRE_RENDER_DISTANCE) / Chunk.CHUNK_Z_SIZE).toInt()
                val xMax = ((camera.position.x + PRE_RENDER_DISTANCE) / Chunk.CHUNK_X_SIZE).toInt()
                val zMax = ((camera.position.z + PRE_RENDER_DISTANCE) / Chunk.CHUNK_Z_SIZE).toInt()

                for (x in xMin until xMax) {
                    for (z in zMin until zMax) {
                        val blocks: MutableMap<Long, Block> = HashMap()
                        val origin = Location(x * Chunk.CHUNK_X_SIZE, 0, z * Chunk.CHUNK_Z_SIZE)

                        if (!usedLocations.contains(origin)) {
                            for (offsetX in 0 until Chunk.CHUNK_X_SIZE) {
                                val nx: Double = (offsetX + x * Chunk.CHUNK_X_SIZE) / featureSize - 0.5
                                for (offsetZ in 0 until Chunk.CHUNK_Z_SIZE) {
                                    val nz: Double = (offsetZ + z * Chunk.CHUNK_Z_SIZE) / featureSize - 0.5
                                    val noise = generator.eval(nx, nz)
                                    val baseHeight = floor(noise * Chunk.CHUNK_Y_SIZE).toInt()

                                    for (y in 0 until baseHeight + Chunk.CHUNK_Y_SIZE) {

                                        val block = Block(0, offsetX, y, offsetZ)
                                        blocks[Chunk.packInternalChunkLocation(offsetX.toLong(), y.toLong(), offsetZ.toLong())] = block
                                    }
                                }
                            }
                            val chunk = Chunk(origin, blocks)
                            world.chunkManager.addChunk(chunk)
                            objects.add(chunk)
                            usedLocations.add(origin)
                        }
                    }
                }
            }
        }
    }

    override fun render(window: Window) {
        renderer.clear()

        GlobalScope.async {
            world.chunkManager.coordinator.update()
        }

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

        world.chunkManager.coordinator.assignNewChunkMeshes()

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
        executorService.shutdown()
    }

    companion object {
        private const val MOUSE_SENSITIVITY = 0.2f
        private const val CAMERA_POS_STEP = 2f
        private const val PRE_RENDER_DISTANCE = 4 * 16
        val executorService: ExecutorService = Executors.newFixedThreadPool(3)
    }
}