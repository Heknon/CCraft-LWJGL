package engine.render

import engine.Window
import engine.model.Camera
import engine.shader.Shader
import engine.shader.shaders.ColorBlendShader
import engine.shader.shaders.TexturedShader
import engine.shader.types.ModelViewMatrixShader
import engine.shader.types.ProjectionMatrixShader
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30

class Renderer {
    private lateinit var shaderMap: Map<String, Shader>
    private lateinit var spaceTransformer3D: SpaceTransformer3D

    fun init() {
        shaderMap = mapOf(Pair("textured", TexturedShader()), Pair("blend", ColorBlendShader()))
        spaceTransformer3D = SpaceTransformer3D()
    }

    fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun render(objects: List<WorldObject3D>, shader: Shader, camera: Camera) {
        shader.start()

        val viewMatrix = spaceTransformer3D.getViewMatrix(camera)

        var currentlyBoundVAO = -1
        var currentlyBoundTexture = -1

        for (obj in objects) {
            if (currentlyBoundVAO != obj.mesh.vertexArrayObjectID) {
                GL30.glBindVertexArray(0)
                GL30.glBindVertexArray(obj.mesh.vertexArrayObjectID)
                currentlyBoundVAO = obj.mesh.vertexArrayObjectID
            }

            if (currentlyBoundTexture != obj.mesh.texture) {
                GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0)
                GL30.glBindTexture(GL30.GL_TEXTURE_2D, obj.mesh.texture)
                currentlyBoundTexture = obj.mesh.texture
            }

            if (shader is ModelViewMatrixShader) {
                val modelViewMatrix = spaceTransformer3D.getModelViewMatrix(
                        obj,
                        viewMatrix
                )

                shader.loadModelViewMatrix(modelViewMatrix)
            }


            GL30.glEnableVertexAttribArray(0)
            GL30.glEnableVertexAttribArray(1)
            GL30.glEnableVertexAttribArray(2)

            GL30.glActiveTexture(GL30.GL_TEXTURE0)

            GL30.glDrawElements(GL30.GL_TRIANGLES, obj.mesh.vertexCount, GL_UNSIGNED_INT, 0)

            GL30.glDisableVertexAttribArray(0)
            GL30.glDisableVertexAttribArray(1)
            GL30.glDisableVertexAttribArray(2)
        }

        GL30.glBindVertexArray(0)
        shader.stop()
    }

    fun render(objects: List<WorldObject3D>, shader: String, camera: Camera) {
        render(objects, getShader(shader), camera)
    }

    fun getShader(shader: String) = shaderMap[shader] ?: error("Invalid shader ID")

    fun updateProjectionMatrix(window: Window) {
        // TODO: Maybe this needs to be loaded every render!
        val projectionMatrix = spaceTransformer3D.getProjectionMatrix(FOV, window.size, Z_NEAR, Z_FAR)
        shaderMap.values.forEach {
            if (it is ProjectionMatrixShader) {
                it.start()
                it.loadProjectionMatrix(projectionMatrix)
                it.stop()
            }
        }
    }

    fun cleanup() {
        shaderMap.values.forEach {
            it.cleanup()
        }
    }

    companion object {
        private val FOV = Math.toRadians(60.0).toFloat()
        private const val Z_NEAR = 0.01f
        private const val Z_FAR = 1000f
    }
}