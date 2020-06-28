package game.world.chunk

import engine.render.WorldObject3D
import game.world.Block
import game.world.Location
import org.joml.Vector3d

class Chunk(origin: Location<Int>, val blocks: Map<Long, Block>) : WorldObject3D(null, Vector3d(origin.x.toDouble(), origin.y.toDouble(), origin.z.toDouble())) {
    var isLoaded: Boolean = false
    var isRebuilt: Boolean = false

    fun load() {
        if (mesh == null) mesh = ChunkMesh(this)
        (mesh as ChunkMesh).load() // isLoaded flag changed internally with this call
    }

    fun getBlockAt(x: Int, y: Int, z: Int): Block? {
        return blocks[packInternalChunkLocation(x.toLong(), y.toLong(), z.toLong())]
    }

    fun rebuild() {
        (mesh as ChunkMesh).rebuild()
    }

    fun assignNewMesh() {
        (mesh as ChunkMesh).assignNewMesh()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Chunk

        if (blocks != other.blocks) return false
        if (isLoaded != other.isLoaded) return false
        if (isRebuilt != other.isRebuilt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = blocks.hashCode()
        result = 31 * result + isLoaded.hashCode()
        result = 31 * result + isRebuilt.hashCode()
        return result
    }


    companion object {
        fun packInternalChunkLocation(a: Long, b: Long, c: Long): Long {
            return a + b * 65536 + c * 65536 * 65536
        }

        fun unpackInternalChunkLocation(packed: Long): LongArray {
            val a: Long = packed shr 32 and 0xFFFFL
            val b: Long = packed shr 16 and 0xFFFFL
            val c: Long = packed and 0xFFFFL
            return longArrayOf(a, b, c)
        }

        const val CHUNK_X_SIZE = 16
        const val CHUNK_Y_SIZE = 16
        const val CHUNK_Z_SIZE = 16

    }
}