package game.world

import engine.Vertex
import org.joml.Vector3f

class ChunkMesh(private val chunk: Chunk) {
    private val vertices: MutableList<Vertex> = mutableListOf()
    lateinit var positions: FloatArray
    lateinit var uvs: FloatArray
    lateinit var normals: FloatArray

    init {
        buildMesh()
        populateLists()
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
                val zEqual = blockI.location.z == blockJ.location.z
                val yEqual = blockI.location.y == blockJ.location.y
                val xEqual = blockI.location.x == blockJ.location.x

                if (blockI.location.x + 1 == blockJ.location.x && yEqual && zEqual) {
                    positiveX = true;
                }

                if (blockI.location.x - 1 == blockJ.location.x && yEqual && zEqual) {
                    negativeX = true;
                }

                if (xEqual && blockI.location.y + 1 == blockJ.location.y && zEqual) {
                    positiveY = true;
                }

                if (xEqual && blockI.location.y - 1 == blockJ.location.y && zEqual) {
                    negativeY = true;
                }

                if (xEqual && yEqual && blockI.location.z + 1 == blockJ.location.z) {
                    positiveZ = true;
                }

                if (xEqual && yEqual && blockI.location.z - 1 == blockJ.location.z) {
                    negativeZ = true;
                }
            }

            // add visible faces to chunk mesh
            if (!positiveX) {
                for (k in 0 until 6) {
                    vertices.add(Vertex(Vector3f(Block.faces.right.vertices[k].x + blockI.location.x, Block.faces.right.vertices[k].y + blockI.location.y, Block.faces.right.vertices[k].z + blockI.location.z), Block.faces.right.textureCoordinates[k], Block.faces.right.vertexNormal[k]))
                }
            }

            if (!negativeX) {
                for (k in 0 until 6) {
                    vertices.add(Vertex(Vector3f(Block.faces.left.vertices[k].x + blockI.location.x, Block.faces.left.vertices[k].y + blockI.location.y, Block.faces.left.vertices[k].z + blockI.location.z), Block.faces.left.textureCoordinates[k], Block.faces.left.vertexNormal[k]))
                }
            }

            if (!positiveY) {
                for (k in 0 until 6) {
                    vertices.add(Vertex(Vector3f(Block.faces.top.vertices[k].x + blockI.location.x, Block.faces.top.vertices[k].y + blockI.location.y, Block.faces.top.vertices[k].z + blockI.location.z), Block.faces.top.textureCoordinates[k], Block.faces.top.vertexNormal[k]))
                }
            }

            if (!negativeY) {
                for (k in 0 until 6) {
                    vertices.add(Vertex(Vector3f(Block.faces.bottom.vertices[k].x + blockI.location.x, Block.faces.bottom.vertices[k].y + blockI.location.y, Block.faces.bottom.vertices[k].z + blockI.location.z), Block.faces.bottom.textureCoordinates[k], Block.faces.bottom.vertexNormal[k]))
                }
            }

            if (!positiveZ) {
                for (k in 0 until 6) {
                    vertices.add(Vertex(Vector3f(Block.faces.far.vertices[k].x + blockI.location.x, Block.faces.far.vertices[k].y + blockI.location.y, Block.faces.far.vertices[k].z + blockI.location.z), Block.faces.far.textureCoordinates[k], Block.faces.far.vertexNormal[k]))
                }
            }

            if (!negativeZ) {
                for (k in 0 until 6) {
                    vertices.add(Vertex(Vector3f(Block.faces.near.vertices[k].x + blockI.location.x, Block.faces.near.vertices[k].y + blockI.location.y, Block.faces.near.vertices[k].z + blockI.location.z), Block.faces.near.textureCoordinates[k], Block.faces.near.vertexNormal[k]))
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
                it % 3 == 0 -> vertices[it / 18].normal.x
                it % 3 == 1 -> vertices[it / 18].normal.y
                it % 3 == 2 -> vertices[it / 18].normal.z
                else -> 0f
            }
        }

        uvs = FloatArray(vertices.size * 2) {
            when {
                it % 2 == 0 -> vertices[it / 12].textureCoordinate.x
                it % 2 == 1 -> vertices[it / 12].textureCoordinate.y
                else -> 0f
            }
        }
    }
}