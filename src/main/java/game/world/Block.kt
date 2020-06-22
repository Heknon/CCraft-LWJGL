package game.world

import org.joml.Vector2f
import org.joml.Vector3f

data class Block(val type: Short, val x: Int, val y: Int, val z: Int) {

    companion object {
        data class Face(
                val vertices: Array<Vector3f>,
                val textureCoordinates: Array<Vector2f>,
                val vertexNormal: Array<Vector3f>
        ) {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Face

                if (!vertices.contentEquals(other.vertices)) return false
                if (!textureCoordinates.contentEquals(other.textureCoordinates)) return false
                if (!vertexNormal.contentEquals(other.vertexNormal)) return false

                return true
            }

            override fun hashCode(): Int {
                var result = vertices.contentHashCode()
                result = 31 * result + textureCoordinates.contentHashCode()
                result = 31 * result + vertexNormal.contentHashCode()
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

        private val sideUV = arrayOf(
                Vector2f(0.0f, 0.0f),
                Vector2f(0.5f, 0.0f),
                Vector2f(0.0f, 0.5f),

                Vector2f(0.0f, 0.5f),
                Vector2f(0.5f, 0.5f),
                Vector2f(0.5f, 0.0f)

        )

        val faces: Faces = Faces(
                top = Face(
                        arrayOf(
                                Vector3f(-0.5f, 0.5f, 0.5f),
                                Vector3f(0.5f, 0.5f, 0.5f),
                                Vector3f(-0.5f, 0.5f, -0.5f),
                                Vector3f(-0.5f, 0.5f, -0.5f),
                                Vector3f(0.5f, 0.5f, -0.5f),
                                Vector3f(0.5f, 0.5f, 0.5f)
                        ),
                        arrayOf(
                                Vector2f(0.0f, 0.5f),
                                Vector2f(0.5f, 0.5f),
                                Vector2f(0.0f, 1.0f),

                                Vector2f(0.0f, 1.0f),
                                Vector2f(0.5f, 1.0f),
                                Vector2f(0.5f, 0.5f)
                        ),
                        arrayOf(
                                Vector3f(0f, 1f, 0f),
                                Vector3f(0f, 1f, 0f),
                                Vector3f(0f, 1f, 0f),
                                Vector3f(0f, 1f, 0f),
                                Vector3f(0f, 1f, 0f),
                                Vector3f(0f, 1f, 0f)
                        )
                ),
                bottom = Face(
                        arrayOf(
                                Vector3f(-0.5f, -0.5f, 0.5f),
                                Vector3f(0.5f, -0.5f, 0.5f),
                                Vector3f(-0.5f, -0.5f, -0.5f),
                                Vector3f(-0.5f, -0.5f, -0.5f),
                                Vector3f(0.5f, -0.5f, -0.5f),
                                Vector3f(0.5f, -0.5f, 0.5f)
                        ),
                        arrayOf(
                                Vector2f(0.5f, 0.0f),
                                Vector2f(1.0f, 0.0f),
                                Vector2f(0.5f, 0.5f),

                                Vector2f(0.5f, 0.5f),
                                Vector2f(1.0f, 0.5f),
                                Vector2f(1.0f, 0.0f)
                        ),
                        arrayOf(
                                Vector3f(0f, 1f, 0f),
                                Vector3f(0f, 1f, 0f),
                                Vector3f(0f, 1f, 0f),
                                Vector3f(0f, 1f, 0f),
                                Vector3f(0f, 1f, 0f),
                                Vector3f(0f, 1f, 0f)
                        )
                ),
                near = Face(
                        arrayOf(
                                Vector3f(-0.5f, 0.5f, -0.5f),
                                Vector3f(0.5f, 0.5f, -0.5f),
                                Vector3f(-0.5f, -0.5f, -0.5f),
                                Vector3f(-0.5f, -0.5f, -0.5f),
                                Vector3f(0.5f, -0.5f, -0.5f),
                                Vector3f(0.5f, 0.5f, -0.5f)
                        ),
                        sideUV,
                        arrayOf(
                                Vector3f(0f, 0f, -1f),
                                Vector3f(0f, 0f, -1f),
                                Vector3f(0f, 0f, -1f),
                                Vector3f(0f, 0f, -1f),
                                Vector3f(0f, 0f, -1f),
                                Vector3f(0f, 0f, -1f)
                        )
                ),
                far = Face(
                        arrayOf(
                                Vector3f(-0.5f, 0.5f, 0.5f),
                                Vector3f(0.5f, 0.5f, 0.5f),
                                Vector3f(-0.5f, -0.5f, 0.5f),
                                Vector3f(-0.5f, -0.5f, 0.5f),
                                Vector3f(0.5f, -0.5f, 0.5f),
                                Vector3f(0.5f, 0.5f, 0.5f)
                        ),
                        sideUV,
                        arrayOf(
                                Vector3f(0f, 0f, -1f),
                                Vector3f(0f, 0f, -1f),
                                Vector3f(0f, 0f, -1f),
                                Vector3f(0f, 0f, -1f),
                                Vector3f(0f, 0f, -1f),
                                Vector3f(0f, 0f, -1f)
                        )
                ),
                left = Face(
                        arrayOf(
                                Vector3f(-0.5f, 0.5f, 0.5f),
                                Vector3f(-0.5f, 0.5f, -0.5f),
                                Vector3f(-0.5f, -0.5f, 0.5f),
                                Vector3f(-0.5f, -0.5f, 0.5f),
                                Vector3f(-0.5f, -0.5f, -0.5f),
                                Vector3f(-0.5f, 0.5f, -0.5f)
                        ),
                        sideUV,
                        arrayOf(
                                Vector3f(1f, 0f, 0f),
                                Vector3f(1f, 0f, 0f),
                                Vector3f(1f, 0f, 0f),
                                Vector3f(1f, 0f, 0f),
                                Vector3f(1f, 0f, 0f),
                                Vector3f(1f, 0f, 0f)
                        )
                ),
                right = Face(
                        arrayOf(
                                Vector3f(0.5f, 0.5f, 0.5f),
                                Vector3f(0.5f, 0.5f, -0.5f),
                                Vector3f(0.5f, -0.5f, 0.5f),
                                Vector3f(0.5f, -0.5f, 0.5f),
                                Vector3f(0.5f, -0.5f, -0.5f),
                                Vector3f(0.5f, 0.5f, -0.5f)
                        ),
                        sideUV,
                        arrayOf(
                                Vector3f(1f, 0f, 0f),
                                Vector3f(1f, 0f, 0f),
                                Vector3f(1f, 0f, 0f),
                                Vector3f(1f, 0f, 0f),
                                Vector3f(1f, 0f, 0f),
                                Vector3f(1f, 0f, 0f)
                        )
                )
        )
    }
}