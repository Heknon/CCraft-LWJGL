package render.shader

import org.joml.Matrix4f

abstract class MatrixShader(vertexShader: String, fragmentShader: String) : Shader(vertexShader, fragmentShader), ProjectableShader, TransformableShader {
    override var transformationMatrixLocation = 0
    override var projectionMatrixLocation = 0

    override fun getAllUniformLocations() {
        transformationMatrixLocation = getUniformLocation("transformationMatrix")
        projectionMatrixLocation = getUniformLocation("projectionMatrix")
    }

    override fun loadTransformationMatrix(matrix: Matrix4f) {
        loadMatrix(transformationMatrixLocation, matrix)
    }

    override fun loadProjectionMatrix(matrix: Matrix4f) {
        loadMatrix(projectionMatrixLocation, matrix)
    }
}