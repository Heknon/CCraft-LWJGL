package game.world.chunk

import game.world.World
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

class ChunkCoordinator(private val world: World) {
    internal val chunksToBuild: Queue<Chunk> = ConcurrentLinkedQueue()
    private val chunksBeingBuilt: MutableSet<Chunk> = ConcurrentHashMap.newKeySet()
    val chunksToRender: MutableSet<Chunk> = ConcurrentHashMap.newKeySet()

    fun update() {
        buildChunks()

        fillRenderableChunks()
    }

    private fun buildChunks() {
        for (i in 0 until MAX_NUMBER_OF_CHUNKS_PER_FRAME) {
            val chunk = chunksToBuild.poll() ?: break
            chunk.load()
            chunksBeingBuilt.add(chunk)
        }
    }

    private fun fillRenderableChunks() {
        for (chunk in world.getChunks()) {
            if (chunk.isLoaded && chunk.isRebuilt) {
                chunksToRender.add(chunk)
            }
        }
    }

    fun assignNewChunkMeshes() {
        for (chunk in chunksBeingBuilt) {
            if (chunk.isLoaded && chunk.isRebuilt) {
                chunk.assignNewMesh()
                chunksBeingBuilt.remove(chunk)
            }
        }
    }

    companion object {
        const val MAX_NUMBER_OF_CHUNKS_PER_FRAME = 5
    }
}