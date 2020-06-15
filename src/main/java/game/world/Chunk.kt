package game.world

import engine.render.WorldObject3D
import org.joml.Vector3f

class Chunk(origin: Location, val blocks: Array<Block>) : WorldObject3D(null, Vector3f(origin.x, origin.y, origin.z)) {
    init {
        mesh = ChunkMesh(this)
    }

    fun getBlockAt(x: Int, y: Int, z: Int): Block {
        return blocks.get(x * y * z)
    }
}