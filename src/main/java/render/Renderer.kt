package render

import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import render.entities.Entity
import render.shader.*
import window.Window
import kotlin.math.tan

class Renderer {
    companion object {
        val shaders: Map<String, Shader> = mapOf(Pair("textured", TexturedShader()), Pair("colorblend", ColorBlendShader()))
        private const val FOV = 70f
        private const val NEAR_PLANE = 0.5f
        private const val FAR_PLANE = 10000f
    }

    private val projectionMatrix: Matrix4f = Matrix4f()

    init {
        createProjectionMatrix()
        shaders.values.forEach {
            if (it is ProjectableShader) {
                it.start()
                it.loadProjectionMatrix(projectionMatrix)
                it.stop()
            }
        }
    }

    fun render(entity: Entity, shaderId: String, useTexture: Boolean = false) {
        val shader = shaders[shaderId]
        val attribAmount = shader?.attributeAmount?.plus(1) ?: 0
        shader?.start()
        GL30.glBindVertexArray(entity.mesh.vertexArrayObjectID)
        for (i in 0..attribAmount) {
            GL20.glEnableVertexAttribArray(i)
        }

        val transformation = Matrix4f().scale(entity.scale, entity.scale, entity.scale).translate(entity.position)
                .rotate(Math.toRadians(entity.rotation.z.toDouble()).toFloat(), Vector3f(0f, 0f, 1f))
                .rotate(Math.toRadians(entity.rotation.y.toDouble()).toFloat(), Vector3f(0f, 1f, 0f))
                .rotate(Math.toRadians(entity.rotation.x.toDouble()).toFloat(), Vector3f(1f, 0f, 0f))
        if (shader is TransformableShader) {
            println(transformation)
            shader.loadTransformationMatrix(transformation)
        }

        if (useTexture) {
            GL20.glActiveTexture(GL13.GL_TEXTURE0)
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.mesh.texture)
        }
        GL11.glDrawElements(GL11.GL_TRIANGLES, entity.mesh.vertexCount, GL11.GL_UNSIGNED_INT, 0)
        for (i in 0..attribAmount) {
            GL20.glDisableVertexAttribArray(i)
        }
        GL30.glBindVertexArray(0)
        shader?.stop()
    }


    private fun createProjectionMatrix() {
        val aspectRatio = Window.INSTANCE?.width!!.toFloat() / Window.INSTANCE?.height!!.toFloat()
        val yScale = (1f / tan(Math.toRadians((FOV / 2f).toDouble()))).toFloat()
        val xScale = yScale / aspectRatio
        val zp = FAR_PLANE + NEAR_PLANE
        val zm = FAR_PLANE - NEAR_PLANE

        projectionMatrix.m00(xScale)
        projectionMatrix.m11(yScale)
        projectionMatrix.m22(-zp / zm)
        projectionMatrix.m23(-1f)
        projectionMatrix.m32(-(2 * FAR_PLANE * NEAR_PLANE) / zm)
        projectionMatrix.m33(0f)
    }
}