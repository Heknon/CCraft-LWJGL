package engine.render

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

data class Mesh(val vertexArrayObjectID: Int, val vertexCount: Int) {
    private var texture: Int = 0

    fun addTexture(texture: String): Mesh {
        this.texture = Texture.loadTexture(texture)
        return this
    }


    fun render() {
        GL30.glEnableVertexAttribArray(0)
        GL30.glBindVertexArray(vertexArrayObjectID)

        GL30.glDrawElements(GL30.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0)

        GL30.glBindVertexArray(0)
        GL30.glDisableVertexAttribArray(0)
    }

    fun cleanup() {
        GL20.glDisableVertexAttribArray(0)
        GL30.glDeleteTextures(texture)

        GL30.glBindVertexArray(0)
        GL30.glDeleteVertexArrays(vertexArrayObjectID)
    }
}