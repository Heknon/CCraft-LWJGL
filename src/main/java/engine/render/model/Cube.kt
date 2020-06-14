package engine.render.model

import engine.render.Mesh
import engine.render.MeshLoader
import engine.render.WorldObject3D
import org.joml.Vector3f

class Cube(
        position: Vector3f = Vector3f(0f, 0f, 0f),
        rotation: Vector3f = Vector3f(0f, 0f, 0f),
        scale: Float = 1f,
        mesh: Mesh = defaultMesh
) : WorldObject3D(mesh, position, rotation, scale) {
    companion object {
        private val positions: FloatArray = floatArrayOf(
                // top face
                -0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,

                // bottom face
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,

                // right face
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,

                // left face
                -0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,

                // near face
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,

                // far face
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f
        )

        private val positionsModeled = floatArrayOf(
                // bottom face
                0.5f, -0.5f, -0.5f, // right bottom near v0
                0.5f, -0.5f, 0.5f,  // right bottom far  v1
                -0.5f, -0.5f, 0.5f, // left bottom far   v2
                -0.5f, -0.5f, -0.5f,// left bottom near  v3

                // top face
                0.5f, 0.5f, -0.5f,  // right top near    v4
                0.5f, 0.5f, 0.5f,   // right top far     v5
                -0.5f, 0.5f, 0.5f,  // left top far      v6
                -0.5f, 0.5f, -0.5f, // left top near     v7

                0.5f, -0.5f, -0.5f, // right bottom near v8
                0.5f, -0.5f, -0.5f, // right bottom near v9
                0.5f, -0.5f, 0.5f,  // right bottom far  v10
                0.5f, -0.5f, 0.5f,  // right bottom far  v11

                -0.5f, -0.5f, -0.5f,// left bottom near  v12
                -0.5f, -0.5f, -0.5f,// left bottom near  v13
                0.5f, 0.5f, -0.5f,  // right top near    v14
                0.5f, 0.5f, -0.5f,  // right top near    v15

                -0.5f, -0.5f, 0.5f, // left bottom far   v16
                -0.5f, -0.5f, 0.5f, // left bottom far   v17
                0.5f, 0.5f, 0.5f,   // right top far     v18
                0.5f, 0.5f, 0.5f,   // right top far     v19

                -0.5f, 0.5f, 0.5f,  // left top far      v20
                -0.5f, 0.5f, 0.5f,  // left top far      v21
                -0.5f, 0.5f, -0.5f, // left top near     v22
                -0.5f, 0.5f, -0.5f  // left top near     v23
        )

        private val indicesModeled = intArrayOf(
                // left face, top far, right angle triangle
                2, 6, 22,

                // near face, bottom left, right angle triangle
                0, 3, 7,

                // bottom far, right triangle
                10, 16, 12,

                // top far, right triangle
                23, 21, 19,

                14, 18, 11,

                2, 6, 22, 0, 3, 7,
                10, 16, 12, 23, 21, 19,
                14, 18, 11, 5, 20, 17,
                8, 10, 12, 15, 23, 19,
                9, 14, 11, 1, 5, 17,
                13, 2, 22, 4, 0, 7
        )

        private val indices = intArrayOf(
                // top face
                0, 1, 2, 2, 3, 0,

                // bottom face
                4, 5, 6, 6, 7, 4,

                // right face
                8, 9, 10, 10, 11, 8,

                // left face
                12, 13, 14, 14, 15, 12,

                // near face
                16, 17, 18, 18, 19, 16,

                // far face
                20, 21, 22, 22, 23, 20
        )

        private val uvs = floatArrayOf(
                0.0f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.0f,
                0.0f, 0.5f, 0.0f,

                0.5f, 0.0f, 0.5f,
                0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f,
                0.0f, 0.0f, 0.5f,

                1.0f, 0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.0f, 0.5f,

                0.0f, 0.0f, 0.5f,
                1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.5f
        )

        private val uvsModeled = floatArrayOf(
                0.0f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.0f,
                0.0f, 0.5f, 0.0f,

                0.5f, 0.0f, 0.5f,
                0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f,
                0.0f, 0.0f, 0.5f,

                1.0f, 0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.0f, 0.5f,

                0.0f, 0.0f, 0.5f,
                1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.5f
        )

        private val normal = floatArrayOf(
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,

                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,

                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,

                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,


                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,

                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f

        )

        private val normalModeled = floatArrayOf(
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, 1.0f,
                -1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, -1.0f,

                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, 1.0f,
                -1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, -1.0f,

                0.0f, -1.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                1.0f, 0.0f, 0.0f,

                0.0f, -1.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,

                0.0f, -1.0f, 0.0f,
                0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,

                0.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f
        )



        data class Face(
                private val vertices: FloatArray,
                private val textureCoordinates: FloatArray,
                private val vertexNormal: FloatArray,
                private val meshIndices: IntArray
        ) {
            val mesh = MeshLoader.createMesh(
                    vertices,
                    textureCoordinates,
                    vertexNormal,
                    meshIndices
            ).addTexture("textures/cube.png").also { println(defaultMesh) }

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Face

                if (!vertices.contentEquals(other.vertices)) return false
                if (!textureCoordinates.contentEquals(other.textureCoordinates)) return false
                if (!vertexNormal.contentEquals(other.vertexNormal)) return false
                if (!meshIndices.contentEquals(other.meshIndices)) return false
                if (mesh != other.mesh) return false

                return true
            }

            override fun hashCode(): Int {
                var result = vertices.contentHashCode()
                result = 31 * result + textureCoordinates.contentHashCode()
                result = 31 * result + vertexNormal.contentHashCode()
                result = 31 * result + meshIndices.contentHashCode()
                result = 31 * result + mesh.hashCode()
                return result
            }


        }

        data class Faces(
                val top: Face,
                val bottom: Face,
                val near: Face,
                val far: Face,
                val left: Face,
                val right: Face
        )

        val faces: Faces = Faces(
                top = Face(
                        floatArrayOf(
                                -0.5f, 0.5f, -0.5f,
                                -0.5f, 0.5f, 0.5f,
                                0.5f, 0.5f, 0.5f,
                                0.5f, 0.5f, -0.5f
                        ),
                        floatArrayOf(
                                1.0f, 0.0f,
                                1.5f, 0.0f,
                                1.0f, 0.5f,

                                1.0f, 0.0f,
                                1.5f, 0.0f,
                                1.0f, 0.5f

                        ),
                        floatArrayOf(0f, 0f, 1f),
                        intArrayOf(0, 1, 2, 2, 3, 0)
                ),
                bottom = Face(
                        floatArrayOf(
                                -0.5f, -0.5f, -0.5f,
                                -0.5f, -0.5f, 0.5f,
                                0.5f, -0.5f, 0.5f,
                                0.5f, -0.5f, -0.5f
                        ),
                        uvsModeled,
                        floatArrayOf(0f, 0f, 1f),
                        intArrayOf(0, 1, 2, 2, 3, 0)
                ),
                near = Face(
                        floatArrayOf(
                                -0.5f, 0.5f, -0.5f,
                                0.5f, 0.5f, -0.5f,
                                0.5f, -0.5f, -0.5f,
                                -0.5f, -0.5f, -0.5f
                        ),
                        uvsModeled,
                        floatArrayOf(0f, 0f, 1f),
                        intArrayOf(0, 1, 2, 2, 3, 0)
                ),
                far = Face(
                        floatArrayOf(
                                -0.5f, 0.5f, 0.5f,
                                0.5f, 0.5f, 0.5f,
                                0.5f, -0.5f, 0.5f,
                                -0.5f, -0.5f, 0.5f
                        ),
                        uvsModeled,
                        floatArrayOf(0f, 0f, 1f),
                        intArrayOf(0, 1, 2, 2, 3, 0)
                ),
                left = Face(
                        floatArrayOf(
                                -0.5f, 0.5f, -0.5f,
                                -0.5f, 0.5f, 0.5f,
                                -0.5f, -0.5f, 0.5f,
                                -0.5f, -0.5f, -0.5f
                        ),
                        uvsModeled,
                        floatArrayOf(0f, 0f, 1f),
                        intArrayOf(0, 1, 2, 2, 3, 0)
                ),
                right = Face(
                        floatArrayOf(
                                0.5f, 0.5f, -0.5f,
                                0.5f, 0.5f, 0.5f,
                                0.5f, -0.5f, 0.5f,
                                0.5f, -0.5f, -0.5f
                        ),
                        uvsModeled,
                        floatArrayOf(0f, 0f, 1f),
                        intArrayOf(0, 1, 2, 2, 3, 0)
                )
        )

        private val defaultMesh: Mesh = MeshLoader.createMesh(
                positionsModeled,
                uvsModeled,
                normalModeled,
                indicesModeled
        ).addTexture("textures/cube.png")

    }
}
