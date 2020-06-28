package game.world.chunk

import game.world.Location

class ChunkManager(val coordinator: ChunkCoordinator) {
    internal val chunks: MutableMap<Long, Chunk> = mutableMapOf()
    val chunksToRender = coordinator.chunksToRender

    fun addChunk(chunk: Chunk) {
        chunks[Location.pack(chunk.position.x, chunk.position.y, chunk.position.z)] = chunk
        coordinator.chunksToBuild.add(chunk)
    }

    fun addChunk(vararg chunks: Chunk) {
        for (chunk in chunks) {
            addChunk(chunk)
        }
    }

    fun getChunk(x: Int, y: Int, z: Int): Chunk? {
        return chunks[Location.pack(x, y, z)]
    }

    fun getChunk(location: Location<Int>): Chunk? {
        return chunks[location.pack()]
    }

    fun removeChunk(location: Location<Int>): Chunk? {
        val packedLocation = location.pack()

        if (!chunks.containsKey(packedLocation)) return null

        val chunk = chunks[packedLocation]
        chunks.remove(packedLocation)
        return chunk
    }
}