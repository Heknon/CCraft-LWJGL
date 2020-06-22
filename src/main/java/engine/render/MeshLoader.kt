package engine.render

import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.nio.FloatBuffer
import java.nio.IntBuffer


object MeshLoader {
    suspend fun createMesh(positions: Array<Vector3f>, uvs: Array<Vector2f>, normals: Array<Vector3f>, indices: IntArray): Mesh {
        val positionsActual = vectorArrayToFloatArray(positions)
        val uvsActual = vectorArrayToFloatArray(uvs)
        val normalsActual = vectorArrayToFloatArray(normals)
        return createMesh(positionsActual, uvsActual, normalsActual, indices)
    }

    fun createMesh(positions: FloatArray, uvs: FloatArray, normals: FloatArray, indices: IntArray?): Mesh {
        val vao = genVertexArrayObjects()
        storeData(0, 3, positions)
        storeData(1, 2, uvs)
        storeData(2, 3, normals)
        if (indices != null) bindIndices(indices)
        GL30.glBindVertexArray(0)
        return Mesh(vao, indices?.size ?: positions.size)
    }

    fun createMesh(positions: FloatArray, uvs: FloatArray, normals: FloatArray): Mesh {
        return createMesh(positions, uvs, normals, null)
    }

    suspend fun createMesh(positions: Array<Vector3f>, uvs: Array<Vector2f>, normals: Array<Vector3f>): Mesh {
        val positionsActual = vectorArrayToFloatArray(positions)
        val uvsActual = vectorArrayToFloatArray(uvs)
        val normalsActual = vectorArrayToFloatArray(normals)
        return createMesh(positionsActual, uvsActual, normalsActual)
    }

    suspend fun createMesh(file: String): Mesh {
        return OBJLoader.loadMesh(file)
    }

    private fun genVertexArrayObjects(): Int {
        val vao = GL30.glGenVertexArrays()
        vertexArrayObjects.add(vao)
        GL30.glBindVertexArray(vao)
        return vao
    }

    private fun storeData(attribute: Int, dimensions: Int, data: FloatArray) {
        val vbo = GL15.glGenBuffers()
        vertexBufferObjects.add(vbo)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        val buffer = createFloatBuffer(data)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(attribute, dimensions, GL11.GL_FLOAT, false, 0, 0)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
    }

    private fun storeData(attribute: Int, dimensions: Int, data: Array<Vector3f>) {
        val array = FloatArray(data.size * 3) {
            when {
                it % 3 == 0 -> data[it / 3].x
                it % 3 == 1 -> data[it / 3].y
                it % 3 == 2 -> data[it / 3].z
                else -> 0f
            }
        }

        storeData(attribute, dimensions, array)
    }

    private fun storeData(attribute: Int, dimensions: Int, data: Array<Vector2f>) {
        val array = FloatArray(data.size * 2) {
            when {
                it % 2 == 0 -> data[it / 2].x
                it % 2 == 1 -> data[it / 2].y
                else -> 0f
            }
        }
        storeData(attribute, dimensions, array)
    }

    private fun bindIndices(data: IntArray) {
        val vbo = GL15.glGenBuffers()
        vertexBufferObjects.add(vbo)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo)
        val buffer = createIntBuffer(data)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
    }

    private fun createFloatBuffer(data: FloatArray): FloatBuffer {
        val buffer: FloatBuffer = BufferUtils.createFloatBuffer(data.size)
        buffer.put(data)
        buffer.flip()
        return buffer
    }

    private fun createIntBuffer(data: IntArray): IntBuffer {
        val buffer: IntBuffer = BufferUtils.createIntBuffer(data.size)
        buffer.put(data)
        buffer.flip()
        return buffer
    }


    fun cleanup() {
        for (vao in vertexArrayObjects) {
            GL30.glDeleteVertexArrays(vao)
        }

        for (vbo in vertexBufferObjects) {
            GL15.glDeleteBuffers(vbo)
        }

        Texture.cleanUp()
    }

    private fun vectorArrayToFloatArray(data: Array<Vector3f>): FloatArray {
        return FloatArray(data.size * 3) {
            when {
                it % 3 == 0 -> data[it / 3].x
                it % 3 == 1 -> data[it / 3].y
                it % 3 == 2 -> data[it / 3].z
                else -> 0f
            }
        }
    }

    private fun vectorArrayToFloatArray(data: Array<Vector2f>): FloatArray {
        return FloatArray(data.size * 2) {
            when {
                it % 2 == 0 -> data[it / 2].x
                it % 2 == 1 -> data[it / 2].y
                else -> 0f
            }
        }
    }

    private val vertexArrayObjects: MutableList<Int> = mutableListOf()
    private val vertexBufferObjects: MutableList<Int> = mutableListOf()

}