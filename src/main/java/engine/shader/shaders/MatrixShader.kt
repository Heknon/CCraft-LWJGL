package engine.shader.shaders

import engine.shader.Shader
import engine.shader.types.ProjectableMatrixShader
import engine.shader.types.WorldMatrixShader
import org.joml.Matrix4f

abstract class MatrixShader(vertexShader: String, fragmentShader: String) : Shader(vertexShader, fragmentShader), ProjectableMatrixShader, WorldMatrixShader {
    override var transformationMatrixLocation = 0
    override var projectionMatrixLocation = 0

    override fun getAllUniformLocations() {
        transformationMatrixLocation = getUniformLocation("worldMatrix")
        projectionMatrixLocation = getUniformLocation("projectionMatrix")
    }

    override fun loadWorldMatrix(matrix: Matrix4f) {
        loadMatrix(transformationMatrixLocation, matrix)
    }

    override fun loadProjectionMatrix(matrix: Matrix4f) {
        loadMatrix(projectionMatrixLocation, matrix)
    }

}