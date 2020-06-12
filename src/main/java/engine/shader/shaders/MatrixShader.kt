package engine.shader.shaders

import engine.shader.Shader
import engine.shader.types.ModelViewMatrixShader
import engine.shader.types.ProjectionMatrixShader
import org.joml.Matrix4f

abstract class MatrixShader(vertexShader: String, fragmentShader: String) : Shader(vertexShader, fragmentShader), ProjectionMatrixShader, ModelViewMatrixShader {
    override var projectionMatrixLocation = 0
    override var modelViewMatrixLocation: Int = 0

    override fun getAllUniformLocations() {
        modelViewMatrixLocation = getUniformLocation("modelViewMatrix")
        projectionMatrixLocation = getUniformLocation("projectionMatrix")
    }


    override fun loadProjectionMatrix(matrix: Matrix4f) {
        loadMatrix(projectionMatrixLocation, matrix)
    }

    override fun loadModelViewMatrix(matrix: Matrix4f) {
        loadMatrix(modelViewMatrixLocation, matrix)
    }

}