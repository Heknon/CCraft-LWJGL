package engine.render

import engine.render.lighting.Material
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import java.util.*

data class Mesh(val vertexArrayObjectID: Int, val vertexCount: Int) {
    var texture: Int = 0
        private set

    val uuid: UUID = UUID.randomUUID()
    var material: Material = Material()

    fun addTexture(texture: String): Mesh {
        this.texture = Texture.loadTexture(texture)
        material.textureId = this.texture
        return this
    }


    fun render() {
        GL30.glEnableVertexAttribArray(0)
        GL30.glBindVertexArray(vertexArrayObjectID)

        GL30.glDrawElements(GL30.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0)

        GL30.glBindVertexArray(0)
        GL30.glDisableVertexAttribArray(0)
    }
}