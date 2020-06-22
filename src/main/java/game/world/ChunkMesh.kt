package game.world

import engine.Vertex
import engine.render.Mesh
import engine.render.MeshHolder
import game.CCraft
import org.joml.Vector3f
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.Future

class ChunkMesh(private val chunk: Chunk) : MeshHolder {
    private val vertices: MutableList<Vertex> = mutableListOf()
    lateinit var positions: FloatArray
    lateinit var uvs: FloatArray
    lateinit var normals: FloatArray
    override var mesh: Mesh? = null

    init {
        services[CCraft.executorService.submit {
            buildMesh()
            println("FINISHED BUILDING MESH")
            populateLists()
        }] = this
//        mesh = GlobalScope.async {
//            MeshLoader.createMesh(
//                    positions,
//                    uvs,
//                    normals
//            ).addTexture("textures/cube.png")
//        }
    }

    fun buildMesh() {
        for (blockI in chunk.blocks) {
            var positiveX = false
            var negativeX = false
            var positiveY = false
            var negativeY = false
            var positiveZ = false
            var negativeZ = false

            for (blockJ in chunk.blocks) {
                // find visible faces
                val zEqual = blockI.z == blockJ.z
                val yEqual = blockI.y == blockJ.y
                val xEqual = blockI.x == blockJ.x

                if (blockI.x + 1 == blockJ.x && yEqual && zEqual) {
                    positiveX = true;
                }

                if (blockI.x - 1 == blockJ.x && yEqual && zEqual) {
                    negativeX = true;
                }

                if (xEqual && blockI.y + 1 == blockJ.y && zEqual) {
                    positiveY = true;
                }

                if (xEqual && blockI.y - 1 == blockJ.y && zEqual) {
                    negativeY = true;
                }

                if (xEqual && yEqual && blockI.z + 1 == blockJ.z) {
                    positiveZ = true;
                }

                if (xEqual && yEqual && blockI.z - 1 == blockJ.z) {
                    negativeZ = true;
                }
            }

            // add visible faces to chunk mesh
            if (!positiveX) {
                for (k in 0 until 6) {
                    vertices.add(Vertex(Vector3f(Block.faces.right.vertices[k].x + blockI.x, Block.faces.right.vertices[k].y + blockI.y, Block.faces.right.vertices[k].z + blockI.z), Block.faces.right.textureCoordinates[k], Block.faces.right.vertexNormal[k]))
                }
            }

            if (!negativeX) {
                for (k in 0 until 6) {
                    vertices.add(Vertex(Vector3f(Block.faces.left.vertices[k].x + blockI.x, Block.faces.left.vertices[k].y + blockI.y, Block.faces.left.vertices[k].z + blockI.z), Block.faces.left.textureCoordinates[k], Block.faces.left.vertexNormal[k]))
                }
            }

            if (!positiveY) {
                for (k in 0 until 6) {
                    vertices.add(Vertex(Vector3f(Block.faces.top.vertices[k].x + blockI.x, Block.faces.top.vertices[k].y + blockI.y, Block.faces.top.vertices[k].z + blockI.z), Block.faces.top.textureCoordinates[k], Block.faces.top.vertexNormal[k]))
                }
            }

            if (!negativeY) {
                for (k in 0 until 6) {
                    vertices.add(Vertex(Vector3f(Block.faces.bottom.vertices[k].x + blockI.x, Block.faces.bottom.vertices[k].y + blockI.y, Block.faces.bottom.vertices[k].z + blockI.z), Block.faces.bottom.textureCoordinates[k], Block.faces.bottom.vertexNormal[k]))
                }
            }

            if (!positiveZ) {
                for (k in 0 until 6) {
                    vertices.add(Vertex(Vector3f(Block.faces.far.vertices[k].x + blockI.x, Block.faces.far.vertices[k].y + blockI.y, Block.faces.far.vertices[k].z + blockI.z), Block.faces.far.textureCoordinates[k], Block.faces.far.vertexNormal[k]))
                }
            }

            if (!negativeZ) {
                for (k in 0 until 6) {
                    vertices.add(Vertex(Vector3f(Block.faces.near.vertices[k].x + blockI.x, Block.faces.near.vertices[k].y + blockI.y, Block.faces.near.vertices[k].z + blockI.z), Block.faces.near.textureCoordinates[k], Block.faces.near.vertexNormal[k]))
                }
            }
        }
    }

    private fun populateLists() {
        positions = FloatArray(vertices.size * 3) {
            when {
                it % 3 == 0 -> vertices[it / 3].position.x
                it % 3 == 1 -> vertices[it / 3].position.y
                it % 3 == 2 -> vertices[it / 3].position.z
                else -> 0f
            }
        }

        normals = FloatArray(vertices.size * 3) {
            when {
                it % 3 == 0 -> vertices[it / 3].normal.x
                it % 3 == 1 -> vertices[it / 3].normal.y
                it % 3 == 2 -> vertices[it / 3].normal.z
                else -> 0f
            }
        }

        uvs = FloatArray(vertices.size * 2) {
            when {
                it % 2 == 0 -> vertices[it / 2].textureCoordinate.x
                it % 2 == 1 -> vertices[it / 2].textureCoordinate.y
                else -> 0f
            }
        }

        vertices.clear()
    }

    companion object {
        val services: ConcurrentMap<Future<*>, ChunkMesh> = ConcurrentHashMap()
    }

}