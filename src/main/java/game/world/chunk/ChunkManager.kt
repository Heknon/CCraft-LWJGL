package game.world.chunk

import game.world.Location

class ChunkManager(val coordinator: ChunkCoordinator) {
    private val chunks: MutableMap<Long, Chunk> = mutableMapOf()

    fun addChunk(chunk: Chunk, location: Location<Int>) {
        chunks[location.pack()] = chunk
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