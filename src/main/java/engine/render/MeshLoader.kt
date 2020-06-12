package engine.render

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.nio.FloatBuffer
import java.nio.IntBuffer

object MeshLoader {
    fun createMesh(positions: FloatArray, uvs: FloatArray, normals: FloatArray, indices: IntArray): Mesh {
        val vao = genVertexArrayObjects()
        storeData(0, 3, positions)
        storeData(1, 2, uvs)
        storeData(2, 3, normals)
        bindIndices(indices)
        GL30.glBindVertexArray(0)
        return Mesh(vao, indices.size)
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

    fun cleanUp() {
        for (vao in vertexArrayObjects) {
            GL30.glDeleteVertexArrays(vao)
        }

        for (vbo in vertexBufferObjects) {
            GL15.glDeleteBuffers(vbo)
        }

        Texture.cleanUp()
    }

    private val vertexArrayObjects: MutableList<Int> = mutableListOf()
    private val vertexBufferObjects: MutableList<Int> = mutableListOf()

}