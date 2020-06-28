package game.world

import game.world.chunk.Chunk
import game.world.chunk.ChunkCoordinator
import game.world.chunk.ChunkManager

abstract class World {
    abstract fun createGenerator()

    val chunkManager: ChunkManager = ChunkManager(ChunkCoordinator(this))

    fun getChunks(): Collection<Chunk> {
        return chunkManager.chunks.values
    }

    fun <T : Number> getChunk(location: Location<T>): Chunk? {
        return getChunk(location.x, location.y, location.z)
    }

    fun <T : Number> getChunk(x: T, y: T, z: T): Chunk? {
        val xInt = x.toInt()
        val yInt = y.toInt()
        val zInt = z.toInt()
        return chunkManager.getChunk(xInt - (xInt % Chunk.CHUNK_X_SIZE), yInt - (yInt % Chunk.CHUNK_Y_SIZE), zInt - (zInt % Chunk.CHUNK_Z_SIZE))
    }

    fun <T : Number> getBlock(x: T, y: T, z: T): Block? {
        val xInt = x.toInt()
        val yInt = y.toInt()
        val zInt = z.toInt()
        val chunk: Chunk? = getChunk(xInt, yInt, zInt)
        return chunk?.getBlockAt(xInt % Chunk.CHUNK_X_SIZE, yInt % Chunk.CHUNK_Y_SIZE, zInt % Chunk.CHUNK_Z_SIZE)
    }

    fun <T : Number> getBlock(location: Location<T>): Block? {
        return getBlock(location.x, location.y, location.z)
    }
}