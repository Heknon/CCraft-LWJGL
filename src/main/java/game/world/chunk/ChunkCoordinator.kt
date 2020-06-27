package game.world.chunk

import game.world.World
import org.joml.Vector3d

class ChunkCoordinator(private val world: World) {
    private val chunksToLoad: MutableList<Chunk> = ArrayList()
    private val chunksToSetup: MutableList<Chunk> = ArrayList()
    private val chunksToRebuild: MutableList<Chunk> = ArrayList()
    private val chunkUpdateFlags: MutableList<Chunk> = ArrayList()
    private var forceVisibilityUpdate = false
    private lateinit var previousCameraPosition: Vector3d
    private lateinit var previousCameraView: Vector3d

    fun update(dt: Float, cameraPosition: Vector3d, cameraView: Vector3d) {
        updateAsyncChunker()
        updateLoadList()
        updateSetupList()
        updateRebuildList()
        updateFlagsList()
        updateUnloadList()
        updateVisibilityList()

        previousCameraPosition = cameraPosition
        previousCameraView = cameraView
    }

    private fun updateAsyncChunker() {

    }

    private fun updateLoadList() {
        var chunksLoaded = 0

        for (chunk in chunksToLoad) {
            if (!chunk.isLoaded) {
                if (chunksLoaded == MAX_NUMBER_OF_CHUNKS_PER_FRAME) break
                chunk.load()
                chunksLoaded++
                forceVisibilityUpdate = true
            }
        }

        chunksToLoad.clear()
    }

    private fun updateSetupList() {
        for (chunk in chunksToSetup) {
            if (chunk.isLoaded && !chunk.isSetup) {
                chunk.setup()

                if (chunk.isSetup) {
                    forceVisibilityUpdate = true
                }
            }
        }

        chunksToSetup.clear()
    }

    private fun updateRebuildList() {
        var chunksRebuilt = 0

        for (chunk in chunksToRebuild) {
            if (!chunk.isLoaded || !chunk.isSetup) continue
            if (chunksRebuilt == MAX_NUMBER_OF_CHUNKS_PER_FRAME) break

            chunk.rebuild()

            chunkUpdateFlags.add(chunk)

            val chunkXMinus = world.getChunk(chunk.position.x - Chunk.CHUNK_X_SIZE, chunk.position.y, chunk.position.z)
            val chunkXPlus = world.getChunk(chunk.position.x + Chunk.CHUNK_X_SIZE, chunk.position.y, chunk.position.z)
            val chunkYMinus = world.getChunk(chunk.position.x, chunk.position.y - Chunk.CHUNK_Y_SIZE, chunk.position.z)
            val chunkYPlus = world.getChunk(chunk.position.x, chunk.position.y + Chunk.CHUNK_Y_SIZE, chunk.position.z)
            val chunkZMinus = world.getChunk(chunk.position.x, chunk.position.y, chunk.position.z - Chunk.CHUNK_Z_SIZE)
            val chunkZPlus = world.getChunk(chunk.position.x, chunk.position.y, chunk.position.z + Chunk.CHUNK_Z_SIZE)

            if (chunkXMinus != null)
                chunkUpdateFlags.add(chunkXMinus)

            if (chunkXPlus != null)
                chunkUpdateFlags.add(chunkXPlus)

            if (chunkYMinus != null)
                chunkUpdateFlags.add(chunkYMinus)

            if (chunkYPlus != null)
                chunkUpdateFlags.add(chunkYPlus)

            if (chunkZMinus != null)
                chunkUpdateFlags.add(chunkZMinus)

            if (chunkZPlus != null)
                chunkUpdateFlags.add(chunkZPlus)


            chunksRebuilt++
            forceVisibilityUpdate = true
        }
    }

    private fun updateFlagsList() {

    }

    private fun updateUnloadList() {

    }

    private fun updateVisibilityList() {

    }

    companion object {
        const val MAX_NUMBER_OF_CHUNKS_PER_FRAME = 5
    }
}