package engine.render

import engine.Window
import engine.render.lighting.PointLight
import engine.render.lighting.Sun
import engine.render.model.Camera
import engine.render.shader.Shader
import engine.render.shader.shaders.ColorBlendShader
import engine.render.shader.shaders.TexturedShader
import engine.render.shader.types.DirectionalLightShader
import engine.render.shader.types.ModelViewMatrixShader
import engine.render.shader.types.PhongShader
import engine.render.shader.types.ProjectionMatrixShader
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30
import java.util.*


class Renderer {
    private lateinit var shaderMap: Map<String, Shader>
    private lateinit var spaceTransformer3D: SpaceTransformer3D
    private var specularPower: Float = 10f

    fun init() {
        shaderMap = mapOf(Pair("textured", TexturedShader()), Pair("blend", ColorBlendShader()))
        spaceTransformer3D = SpaceTransformer3D()
        shaderMap.values.forEach {
            if (it is PhongShader) {
                it.start()
                it.loadSpecularPower(specularPower)
                it.stop()
            }
        }
    }

    fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun render(
            objects: List<WorldObject3D>,
            shader: Shader,
            camera: Camera,
            ambientLight: Vector3f,
            pointLight: PointLight,
            sun: Sun
    ) {
        shader.start()

        val viewMatrix = spaceTransformer3D.getViewMatrix(camera)


        if (shader is PhongShader) {
            val currPointLight = PointLight(pointLight)
            val lightPos: Vector3f = currPointLight.position
            val aux = Vector4f(lightPos, 1f)
            aux.mul(viewMatrix)
            lightPos.x = aux.x
            lightPos.y = aux.y
            lightPos.z = aux.z

            shader.loadAmbientLight(ambientLight)
            shader.loadPointLight(currPointLight)
        }

        if (shader is DirectionalLightShader) {
            val currDirectionalLight = sun.directionalLightClone
            val dir = Vector4f(currDirectionalLight.direction, 0f)
            dir.mul(viewMatrix)
            currDirectionalLight.direction = Vector3f(dir.x, dir.y, dir.z)
            shader.loadDirectionLight(currDirectionalLight)
        }


        var currentlyBoundVAO = -1
        var currentlyBoundTexture = -1
        var currentMesh: UUID? = null

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

            if (currentMesh != obj.mesh.uuid && shader is PhongShader) {
                shader.loadMaterial(obj.mesh.material)
                currentMesh = obj.mesh.uuid
            }


            GL30.glEnableVertexAttribArray(0)
            GL30.glEnableVertexAttribArray(1)
            GL30.glEnableVertexAttribArray(2)

            GL30.glActiveTexture(GL30.GL_TEXTURE0)

            //GL30.glDrawElements(GL30.GL_TRIANGLES, obj.mesh.vertexCount, GL_UNSIGNED_INT, 0)
            glDrawArrays(GL_TRIANGLES, 0, obj.mesh.vertexCount)

            GL30.glDisableVertexAttribArray(0)
            GL30.glDisableVertexAttribArray(1)
            GL30.glDisableVertexAttribArray(2)
        }

        GL30.glBindVertexArray(0)
        shader.stop()
    }

    fun render(
            objects: List<WorldObject3D>,
            shader: String,
            camera: Camera,
            ambientLight: Vector3f,
            pointLight: PointLight,
            sun: Sun
    ) {
        render(objects, getShader(shader), camera, ambientLight, pointLight, sun)
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