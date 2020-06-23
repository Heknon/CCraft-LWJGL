package game.world

import engine.render.WorldObject3D
import game.CCraft
import org.joml.Vector3f

class Chunk(origin: Location, val blocks2: Array<Block>) : WorldObject3D(null, Vector3f(origin.x, origin.y, origin.z)) {
    val blocks: Map<Long, Block> = blocks2.associateBy { packInternalChunkLocation(it.x.toLong(), it.y.toLong(), it.z.toLong()) }

    init {
        mesh = ChunkMesh(this)
    }

    fun getBlockAt(x: Int, y: Int, z: Int): Block? {
        return blocks[packInternalChunkLocation(x.toLong(), y.toLong(), z.toLong())]
    }

    private fun packInternalChunkLocation(a: Long, b: Long, c: Long): Long {
        val n: Long = 65536
        return a + b * n + c * n * n
    }

    private fun unpackInternalChunkLocation(packed: Long): LongArray {
        val a: Long = packed shr 32 and 0xFFFFL
        val b: Long = packed shr 16 and 0xFFFFL
        val c: Long = packed and 0xFFFFL
        return longArrayOf(a, b, c)
    }
}